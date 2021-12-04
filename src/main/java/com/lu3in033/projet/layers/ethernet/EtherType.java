package com.lu3in033.projet.layers.ethernet;

public class EtherType {
    private final short type;

    EtherType(short t) {
        type = t;
    }

    public short value() {
        return type;
    }

    String name() {
        return EtherTypes.valueNameFor(type).orElse("Unknown");
    }
}
