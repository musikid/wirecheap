package com.lu3in033.projet.layers.ethernet;

public class EtherType {
    private final short type;

    EtherType(short t) {
        type = t;
    }

    public short type() {
        return type;
    }
}
