package com.lu3in033.projet.parser;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class StatefulFragment extends Fragment {
    public final int id;

    public StatefulFragment(int offset, List<Byte> buffer, int id) {
        super(offset, buffer);
        this.id = id;
    }

    public StatefulFragment(Fragment f, int id) {
        this(f.offset, f.buffer, id);
    }

    @Override
    public String toString() {
        return new StringJoiner("\n\t", StatefulFragment.class.getSimpleName() + " {\n\t", "\n}")
                .add(String.format("Offset: 0x%02x", offset))
                .add("Buffer: " + buffer.stream().map(b -> String.format("%02x", b)).collect(Collectors.toList()))
                .add(String.format("Frame number: %d", id)).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        StatefulFragment fragment = (StatefulFragment) o;
        return offset == fragment.offset && id == fragment.id && buffer.equals(fragment.buffer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, buffer);
    }
}
