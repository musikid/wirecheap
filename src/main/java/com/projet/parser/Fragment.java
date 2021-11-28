package com.projet.parser;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Fragment {
    public final int offset;
    public final List<Byte> buffer;

    public Fragment(int off, List<Byte> buf) {
        offset = off;
        buffer = buf;
    }

    @Override
    public String toString() {
        return new StringJoiner("\n\t", Fragment.class.getSimpleName() + " {\n\t", "\n}")
                .add(String.format("Offset: 0x%02x", offset))
                .add("Buffer: " + buffer.stream().map(b -> String.format("%02x", b)).collect(Collectors.toList()))
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fragment fragment = (Fragment) o;
        return offset == fragment.offset && buffer.equals(fragment.buffer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, buffer);
    }
}
