package com.lu3in033.projet.layers.ipv4;

import com.lu3in033.projet.layers.ethernet.EtherTypes;

import java.util.Arrays;
import java.util.Optional;

public enum NextHeaderProtocols {
    UDP(17);

    private final byte value;

    NextHeaderProtocols(int v) {
        value = (byte) v;
    }

    public byte value() {
        return value;
    }

    public static Optional<String> valueNameFor(int t) {
        return Arrays.stream(EtherTypes.values()).filter(v -> v.value() == t).findFirst().map(EtherTypes::name);
    }
}
