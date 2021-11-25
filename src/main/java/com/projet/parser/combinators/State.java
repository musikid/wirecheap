package com.projet.parser.combinators;

import java.util.Collections;
import java.util.Optional;

public class State<S extends CharSequence> {
    private static final char NEWLINE = '\n';

    private final S source;
    private final int len;
    private int pos = 0;
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

    public Object getResult() {
        return result;
    }

    public void setResult(Object res) {
        result = res;
    }

    public Location getLocation() {
        CharSequence before = source.subSequence(0, pos - 1);
        long line = before.chars().filter(c -> c == NEWLINE).count() + 1;
        long col = pos - before.chars().boxed().sorted(Collections.reverseOrder()).takeWhile(c -> c != NEWLINE).count();
        return new Location(line, col);
    }
}
