package com.lu3in033.projet.parser.combinators;

public class ParseException extends Exception {
    private final long line;
    private final long column;

    public ParseException(long l, long c) {
        line = l;
        column = c;
    }

    @Override
    public String getMessage() {
        return String.format("Error at line %d, character %d", line, column);
    }
}
