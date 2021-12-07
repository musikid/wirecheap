package com.lu3in033.projet.parser;

import com.lu3in033.projet.parser.combinators.Checkpoint;
import com.lu3in033.projet.parser.combinators.Combinator;
import com.lu3in033.projet.parser.combinators.ParseException;
import com.lu3in033.projet.parser.combinators.State;

import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lu3in033.projet.parser.combinators.Combinators.*;

public class Parser {
    private int dataLength = 0;
    private int frameId = 0;

    public Parser() {

    }

    static Combinator<Void> commentLine() {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                Object result = state.getResult();
                if (hexOffset().apply(state))
                    return false;
                if (!skipTo(newline()).apply(state))
                    return false;

                state.setResult(result);

                return true;
            }
        };
    }

    static Combinator<Fragment> fragment() {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                if (!hexOffset().apply(state))
                    return false;

                int offset = hexOffset().getResult(state);

                // We skip spaces
                spaces().apply(state);

                // Parse the bytes
                Combinator<List<Byte>> bytesParser = many1(hexByte().skip(space()));
                if (!bytesParser.apply(state))
                    return false;

                List<Byte> bytes = bytesParser.getResult(state);

                if (!space().apply(state))
                    return false;

                // We skip everything until newline
                skipTo(newline()).apply(state);

                Fragment fragment = new Fragment(offset, bytes);
                state.setResult(fragment);

                return true;
            }
        };
    }

    Combinator<StatefulFragment> statefulFragment() {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                Checkpoint c = state.checkpoint();

                if (!fragment().apply(state)) {
                    return false;
                }

                Fragment f = fragment().getResult(state);

                // If we have a new frame
                if (f.offset == 0) {
                    dataLength = f.buffer.size();
                    frameId++;
                    state.setResult(new StatefulFragment(f, frameId));
                    return true;
                }

                // If the current offset equals the previous data length
                if (dataLength == f.offset) {
                    dataLength += f.buffer.size();
                    state.setResult(new StatefulFragment(f, frameId));
                    return true;
                }

                state.restore(c);
                return false;
            }
        };
    }

    Combinator<List<StatefulFragment>> fragments() {
        return manyTill(statefulFragment().skip(many(commentLine())), eof());
    }

    // TODO: Java streams are slow compared to loops
    public List<Frame> parse(String buffer) throws ParseException {
        this.reset();

        List<StatefulFragment> fragments = fragments().parse(buffer);
        Map<Integer, List<StatefulFragment>> map = fragments.stream().collect(Collectors.groupingBy(f -> f.id));

        return map.entrySet().stream().map(e -> {
            int id = e.getKey();
            List<StatefulFragment> statefulFragments = e.getValue();
            StatefulFragment maxFragment = statefulFragments.stream().max(Comparator.comparingInt(f -> f.offset)).get();
            int bufferSize = maxFragment.offset + maxFragment.buffer.size();
            ByteBuffer mergedBuffer = statefulFragments.stream().flatMap(f -> f.buffer.stream()).collect(
                    () -> ByteBuffer.allocate(bufferSize),
                    ByteBuffer::put, ByteBuffer::put).rewind();

            return new Frame(id, mergedBuffer);
        }).collect(Collectors.toList());
    }

    void reset() {
        dataLength = 0;
        frameId = 0;
    }
}