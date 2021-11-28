package com.lu3in033.projet.parser;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Frame {
    public final int id;
    public final List<Byte> buffer;

    public Frame(int id, List<Byte> buf) {
        this.id = id;
        buffer = buf;
    }

    @Override
    public String toString() {
        return new StringJoiner("\n\t", Frame.class.getSimpleName() + " {\n\t", "\n}")
                .add(String.format("Frame ID: 0x%02x", id))
                .add("Buffer: " + buffer.stream().map(b -> String.format("0x%02x", b)).collect(Collectors.toList()))
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Frame frame = (Frame) o;
        return id == frame.id && buffer.equals(frame.buffer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, buffer);
    }
}