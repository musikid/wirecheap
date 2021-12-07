package com.lu3in033.projet.layers.ethernet;

import java.util.Arrays;

public class EtherType {
    private final short type;

    EtherType(short t) {
        type = t;
    }

    public short value() {
        return type;
    }

    String name() {
        return Arrays.stream(EtherTypes.values()).filter(v -> v.value() == type)
                .findFirst().map(EtherTypes::name).orElse("Unknown");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EtherType etherType = (EtherType) o;

        return type == etherType.type;
    }

    @Override
    public int hashCode() {
        return type;
    }
}
