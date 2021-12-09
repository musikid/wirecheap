package com.lu3in033.projet.parser.combinators;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Combinators {
    /**
     * Parses a character if it satisfies <i>f</i>.
     *
     * @return Combinator&lt;Character&gt;
     */
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
     * @return Combinator&lt;Character&gt;
     */
    public static Combinator<Character> hexDigit() {
        return satisfy(c -> Character.isDigit(c) || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F');
    }

    // TODO: Use a loop instead

    /**
     * Apply the combinator <i>cb</i> <i>min</i> or more times.
     *
     * @param combinator Combinator to repeat
     * @param <O>        Combinator output
     * @return Combinator&lt;List&lt;O&gt;&gt;
     */
    public static <O> Combinator<List<O>> count(int min, Combinator<O> combinator) {
        return count(min, Long.MAX_VALUE, combinator);
    }

    /**
     * Apply the combinator <i>cb</i> between <i>min</i> and <i>max</i> times.
     *
     * @param combinator Combinator to repeat
     * @param <O>        Combinator output
     * @return Combinator&lt;List&lt;O&gt;&gt;
     */
    public static <O> Combinator<List<O>> count(int min, long max, Combinator<O> combinator) {
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
     * @return Combinator&lt;Integer&gt;
     * @see Combinators#hexDigit()
     */
    public static Combinator<Integer> hexOffset() {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                Combinator<List<Character>> c = count(2, hexDigit());
                if (!c.apply(state)) {
                    state.setExpected("Valid offset");
                    return false;
                }
                String offsetString = c.getResult(state).stream().map(String::valueOf).collect(Collectors.joining());

                state.setResult(Integer.parseInt(offsetString, 16));
                return true;
            }
        };
    }

    /**
     * Parses a byte from a string of format "FF".
     *
     * @return Combinator&lt;Byte&gt;
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

    public static <O> Combinator<List<O>> sep_by1(Combinator<O> cb, Combinator<?> delim) {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<?> state) {
                if (!cb.apply(state))
                    return false;

                O first = cb.getResult(state);

                Combinator<List<O>> parser = many(delim.then(cb));

                if (!parser.apply(state))
                    return false;

                List<O> l = parser.getResult(state);
                l.add(0, first);
                state.setResult(l);
                return true;
            }
        };
    }

    /**
     * Parses the space character.
     *
     * @return Combinator&lt;Character&gt;
     * @see Combinators#hexDigit()
     */
    public static Combinator<Character> space() {
        return satisfy(c -> c == ' ');
    }

    public static Combinator<List<Character>> spaces() {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                return many(space()).apply(state);
            }
        };
    }

    /**
     * Apply <i>skip</i> while it succeeds and skip its result.
     *
     * @param skip Skip combinator
     * @return Combinator&lt;Void&gt;
     */
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

    /**
     * Apply the combinator <i>cb</i> zero or more times.
     *
     * @param cb  Combinator to repeat
     * @param <O> Combinator output
     * @return Combinator&lt;List&lt;O&gt;&gt;
     */
    public static <O> Combinator<List<O>> many(Combinator<O> cb) {
        return count(0, cb);
    }

    /**
     * Apply the combinator <i>cb</i> until it fails and succeeds when <i>delim</i> immediately follow it.
     *
     * @param cb    Combinator to repeat
     * @param <O>   Combinator output
     * @param delim Delimiter
     * @return Combinator&lt;List&lt;O&gt;&gt;
     */
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

    /**
     * Apply the combinator <i>cb</i> until it fails and succeeds when <i>delim</i> immediately follow it.
     *
     * @return Combinator&lt;Void&gt;
     */
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
