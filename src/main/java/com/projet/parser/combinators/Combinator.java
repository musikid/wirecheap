package com.projet.parser.combinators;

import java.util.function.Function;

public abstract class Combinator<O> implements Function<State<? extends CharSequence>, Boolean> {
    public abstract Boolean apply(State<? extends CharSequence> state);

    public O parse(String s) throws ParseException {
        return this.parse(new State<>(s));
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
