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
}
