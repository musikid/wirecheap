package com.lu3in033.projet.parser.combinators;

public class ParseException extends Exception {
    public final int line;
    public final int column;
    public final int rawPos;
    public final String content;
    public final String expected;

    public ParseException(int l, int c, int rawPos, String content, String expected) {
        line = l;
        column = c;
        this.rawPos = rawPos;
        this.content = content;
        this.expected = expected;
    }

    @Override
    public String getMessage() {
        return String.format("Error at line %d, character %d: expected %s, found %s", line, column,
                expected, content.substring(rawPos, rawPos + 20));
    }
}
