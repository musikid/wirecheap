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
                if (!hexOffset().apply(state)) {
                    state.setExpected("valid offset");
                    return false;
                }
                int offset = hexOffset().getResult(state);

                // We skip spaces
                if (!spaces().apply(state)) {
                    state.setExpected("space(s) between offset and bytes");
                    return false;
                }

                // Parse the bytes
                Combinator<List<Byte>> bytesParser = sep_by1(hexByte(), space());
                if (!bytesParser.apply(state)) {
                    state.setExpected("at least one byte");
                    return false;
                }
                List<Byte> bytes = bytesParser.getResult(state);

                // We check if it's the end of line,
                // if it isn't then we have a misleading byte
                Checkpoint c = state.checkpoint();
                if (!space().apply(state)) {
                    state.restore(c);
                    if (!newline().apply(state)) {
                        state.setExpected("valid byte (0x00-0xFF)");
                        state.restore(c);
                        return false;
                    }
                } else {
                    // Inline comments starts with two spaces
                    if (!space().apply(state)) {
                        state.setExpected("valid byte (0x00-0xFF)");
                        return false;
                    }

                    // We skip everything until newline
                    skipTo(newline()).apply(state);
                }

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

                state.setExpected(String.format("valid offset: 0x%1$04x (%1$d) do not overlap %2$d bytes", f.offset, dataLength));
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