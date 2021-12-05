package com.lu3in033.projet.parser;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class Frame {
    public final int id;
    public final ByteBuffer buffer;

    public Frame(int id, ByteBuffer buf) {
        this.id = id;
        buffer = buf;
    }

    @Override
    public String toString() {
        return new StringJoiner("\n\t", Frame.class.getSimpleName() + " {\n\t", "\n}")
                .add(String.format("Frame ID: 0x%02x", id))
                .add("Buffer: " + Arrays.toString(buffer.array()))
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Frame frame = (Frame) o;
        return id == frame.id && buffer.equals(frame.buffer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, buffer);
    }
}