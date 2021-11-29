package com.lu3in033.projet.layers;

public class NotEnoughBytesException extends Exception {
    private final int expected;
    private final int got;

    public NotEnoughBytesException(int e, int g) {
        expected = e;
        got = g;
    }

    @Override
    public String getMessage() {
        return String.format("Not enough bytes: expected %d, got %d", expected, got);
    }
}
