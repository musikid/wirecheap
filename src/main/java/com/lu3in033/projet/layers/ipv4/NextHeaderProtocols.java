package com.lu3in033.projet.layers.ipv4;

public enum NextHeaderProtocols {
    Udp(17);

    private final byte value;

    NextHeaderProtocols(int v) {
        value = (byte) v;
    }

    public byte value() {
        return value;
    }
}
