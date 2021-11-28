package com.lu3in033.projet.parser.combinators;

import java.util.function.Function;

public abstract class Combinator<O> implements Function<State<? extends CharSequence>, Boolean> {
    public abstract Boolean apply(State<? extends CharSequence> state);

    public O parse(String s) throws ParseException {
        return this.parse(new State<>(s));
    }

    public Combinator<O> skip(Combinator<?> skipCombinator) {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                if (Combinator.this.apply(state)) {
                    O res = Combinator.this.getResult(state);
                    if (skipCombinator.apply(state)) {
                        state.setResult(res);
                        return true;
                    }
                }
                return false;
            }
        };
    }

    @SuppressWarnings("unchecked")
    public O getResult(State<? extends CharSequence> state) {
        return (O) state.getResult();
    }

    public final O parse(State<? extends CharSequence> state) throws ParseException {
        if (this.apply(state))
            return this.getResult(state);

        Location l = state.getLocation();
        throw new ParseException(l.line, l.column);
    }
}
