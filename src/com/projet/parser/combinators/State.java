package com.projet.parser.combinators;

import java.util.Optional;

public class State<S extends CharSequence> {
    final private S source;
    private int pos = 0;
    private int len;
    private Object result;

    public State(S src) {
        source = src;
        len = source.length();
    }

    public int checkpoint() {
        return pos;
    }

    public boolean isEof() {
        return next().isEmpty();
    }

    public Optional<Character> next() {
        return (pos < len) ? Optional.of(source.charAt(pos++)) : Optional.empty();
    }

    public void restore(int checkpoint) throws StringIndexOutOfBoundsException {
        // TODO: Add bound checks
        if (checkpoint < 0 || checkpoint >= len) {
            throw new StringIndexOutOfBoundsException(checkpoint);
        }
        pos = checkpoint;
    }

    public void setResult(Object res) {
        result = res;
    }

    public Object getResult() {
        return result;
    }
}
