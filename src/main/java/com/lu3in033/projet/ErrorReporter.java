package com.lu3in033.projet;

import java.util.StringJoiner;

public class ErrorReporter {
    public String extract;
    public int extractStart;
    public int col;
    public int line;
    public String expected;

    public ErrorReporter(String extract, int extractStart, int col, int line, String expected) {
        this.extract = extract;
        this.extractStart = extractStart;
        this.col = col;
        this.line = line;
        this.expected = expected;
    }

    @Override
    public String toString() {
        return new StringJoiner("\n")
                .add(String.format("Error at line %d, character %d", line, col))
                .add(extract)
                .add(String.format("%" + (col - extractStart) + "s", "^"))
                .add("Expected: " + expected)
                .toString();
    }
}
