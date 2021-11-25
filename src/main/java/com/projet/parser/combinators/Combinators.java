package com.projet.parser.combinators;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Combinators {
    public static Combinator<Character> satisfy(Predicate<Character> f) {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<?> state) {
                Optional<Character> c = state.next();
                boolean r = c.map(f::test).orElse(false);
                if (r)
                    state.setResult(c);

                return r;
            }
        };
    }

    public static Combinator<Character> hexDigit() {
        return satisfy(c -> Character.isDigit(c)
                || c >= 'A' && c <= 'F'
                || c >= 'a' && c <= 'f');
    }

    @SuppressWarnings("unchecked")
    public static <O> Combinator<List<O>> count(int min, Combinator<O> combinator) {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                List<O> l = new ArrayList<>();
                while (combinator.apply(state))
                    l.add((O) state.getResult());

                if (l.size() < min)
                    return false;

                state.setResult(l);
                return true;
            }
        };
    }

    public static <O> Combinator<List<O>> count(int min, int max, Combinator<O> combinator) {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                List<O> l = new ArrayList<>();
                int i = 0;
                while (combinator.apply(state) && i < max) {
                    i++;
                    @SuppressWarnings("unchecked")
                    O result = (O) state.getResult();
                    l.add(result);
                }

                if (l.size() < min)
                    return false;

                state.setResult(l);
                return true;
            }
        };
    }

    public static Combinator<Byte> hexByte() {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                if (!count(2, 2, hexDigit()).apply(state))
                    return false;

                @SuppressWarnings("unchecked")
                String byteString = ((List<Character>) state.getResult())
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining());

                state.setResult(Byte.parseByte(byteString, 16));
                return true;
            }
        };
    }
}
