package com.lu3in033.projet.parser;

import com.lu3in033.projet.parser.combinators.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                if (Combinators.hexOffset().apply(state))
                    return false;
                if (!Combinators.skipTo(Combinators.newline()).apply(state))
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
                if (!Combinators.hexOffset().apply(state))
                    return false;

                int offset = Combinators.hexOffset().getResult(state);

                // We skip spaces
                Combinators.spaces().apply(state);

                // Parse the bytes
                Combinator<List<Byte>> bytesParser = Combinators.many1(Combinators.hexByte().skip(Combinators.space()));
                if (!bytesParser.apply(state))
                    return false;

                List<Byte> bytes = bytesParser.getResult(state);

                if (!Combinators.space().apply(state))
                    return false;

                // We skip everything until newline
                Combinators.skipTo(Combinators.newline()).apply(state);

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

                if (f.offset == 0) {
                    dataLength = f.buffer.size();
                    frameId++;
                    state.setResult(new StatefulFragment(f, frameId));
                    return true;
                }

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
        return Combinators.manyTill(statefulFragment().skip(Combinators.many(commentLine())), Combinators.eof());
    }

    // TODO: Java streams are slow compared to loops
    public List<Frame> parse(String buffer) throws ParseException {
        dataLength = 0;
        frameId = 0;

        List<StatefulFragment> fragments = fragments().parse(buffer);
        Map<Integer, List<StatefulFragment>> map = fragments.stream().collect(Collectors.groupingBy(f -> f.id));
        return map.entrySet().stream()
                .map(e -> {
                            int id = e.getKey();
                            List<Byte> mergedBuffer = e.getValue().stream().flatMap(f -> f.buffer.stream()).collect(Collectors.toList());
                            return new Frame(id, mergedBuffer);
                        }
                )
                .collect(Collectors.toList());
    }
}