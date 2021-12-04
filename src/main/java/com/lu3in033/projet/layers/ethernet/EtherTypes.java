package com.lu3in033.projet.layers.ethernet;

public enum EtherTypes {
    IPv4(0x0800);

    private final int value;

    EtherTypes(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
