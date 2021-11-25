package com.projet.parser.combinators;

import java.util.Optional;
import java.util.function.Predicate;

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
}
