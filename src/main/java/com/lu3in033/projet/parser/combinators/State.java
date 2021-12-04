package com.lu3in033.projet.parser.combinators;

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

    public Checkpoint checkpoint() {
        return new Checkpoint(pos);
    }

    public boolean isEof() {
        return pos == len;
    }

    public Optional<Character> next() {
        return (pos < len) ? Optional.of(source.charAt(pos++)) : Optional.empty();
    }

    public void restore(Checkpoint c) throws StringIndexOutOfBoundsException {
        // TODO: Add bound checks
        if (c.pos < 0 || c.pos > len) {
            throw new StringIndexOutOfBoundsException(c.pos);
        }
        pos = c.pos;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object res) {
        result = res;
    }

    public Location getLocation() {
        if (len == 0) {
            return new Location(1, 0);
        }

        String before = source.subSequence(0, pos).toString();
        long line = before.chars().filter(c -> c == NEWLINE).count() + 1;
        long col = pos - before.lastIndexOf(NEWLINE);
        return new Location(line, col);

    }
}
