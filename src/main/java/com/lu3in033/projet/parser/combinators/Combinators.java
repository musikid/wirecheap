package com.lu3in033.projet.parser.combinators;

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

    /**
     * Parses an hexadecimal digit.
     *
     * @return Combinator<Character>
     */
    public static Combinator<Character> hexDigit() {
        return satisfy(c -> Character.isDigit(c) || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f');
    }

    // TODO: Use a loop instead
    public static <O> Combinator<List<O>> count(int min, Combinator<O> combinator) {
        return count(min, Integer.MAX_VALUE, combinator);
    }

    public static <O> Combinator<List<O>> count(int min, int max, Combinator<O> combinator) {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                List<O> l = new ArrayList<>();
                int i = 0;
                Checkpoint c = state.checkpoint();

                while (combinator.apply(state) && i < max) {
                    c = state.checkpoint();
                    i++;
                    O result = combinator.getResult(state);
                    l.add(result);
                }

                state.restore(c);

                if (l.size() < min)
                    return false;

                state.setResult(l);
                return true;
            }
        };
    }

    /**
     * Parses an offset (an <i>Integer</i>) from a string of hexadecimal digits (with length > 2), for example "0000".
     *
     * @return Combinator<Integer>
     * @see Combinators#hexDigit()
     */
    public static Combinator<Integer> hexOffset() {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                Combinator<List<Character>> c = count(2, hexDigit());
                if (!c.apply(state))
                    return false;

                String offsetString = c.getResult(state).stream().map(String::valueOf).collect(Collectors.joining());

                state.setResult(Integer.parseInt(offsetString, 16));
                return true;
            }
        };
    }

    /**
     * Parses a byte from a string of format "FF".
     *
     * @return Combinator<Byte>
     *
     * @see Combinators#hexDigit()
     */
    public static Combinator<Byte> hexByte() {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                Combinator<List<Character>> c = count(2, 2, hexDigit());
                if (!c.apply(state))
                    return false;

                String byteString = c.getResult(state).stream().map(String::valueOf).collect(Collectors.joining());

                state.setResult((byte) Integer.parseInt(byteString, 16));
                return true;
            }
        };
    }

    public static Combinator<Character> space() {
        return satisfy(c -> c == ' ');
    }

    public static Combinator<List<Character>> spaces() {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                many(space()).apply(state);

                return true;
            }
        };
    }

    public static Combinator<Void> skipTo(Combinator<?> skip) {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                while (!skip.apply(state) && !eof().apply(state))
                    ;

                return !eof().apply(state);
            }
        };
    }

    public static <O> Combinator<List<O>> many(Combinator<O> cb) {
        return count(0, cb);
    }

    public static <O> Combinator<List<O>> many1(Combinator<O> cb) {
        return count(1, cb);
    }

    public static <O> Combinator<List<O>> manyTill(Combinator<O> cb, Combinator<?> delim) {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                List<O> l = new ArrayList<>();

                while (cb.apply(state))
                    l.add(cb.getResult(state));

                if (!delim.apply(state)) {
                    return false;
                }

                state.setResult(l);
                return true;
            }
        };
    }

    public static Combinator<Void> eof() {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                return state.isEof();
            }
        };
    }

    public static Combinator<Character> newline() {
        return satisfy(c -> c == '\n');
    }

}
