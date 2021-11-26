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
                    state.setResult(c.get());

                return r;
            }
        };
    }

    public static Combinator<Character> hexDigit() {
        return satisfy(c -> Character.isDigit(c)
                || c >= 'A' && c <= 'F'
                || c >= 'a' && c <= 'f');
    }

    public static <O> Combinator<List<O>> count(int min, Combinator<O> combinator) {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                List<O> l = new ArrayList<>();
                while (combinator.apply(state))
                    l.add(combinator.getResult(state));

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
                    O result = combinator.getResult(state);
                    l.add(result);
                }

                if (l.size() < min)
                    return false;

                state.setResult(l);
                return true;
            }
        };
    }

    public static Combinator<Integer> hexOffset() {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                Combinator<List<Character>> c = count(2, hexDigit());
                if (!c.apply(state))
                    return false;

                String offsetString = c.getResult(state)
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining());

                state.setResult(Integer.parseInt(offsetString, 16));
                return true;
            }
        };
    }

    public static Combinator<Byte> hexByte() {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                Combinator<List<Character>> c = count(2, 2, hexDigit());
                if (!c.apply(state))
                    return false;

                String byteString = c.getResult(state)
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining());

                state.setResult((byte) Integer.parseInt(byteString, 16));
                return true;
            }
        };
    }
}
