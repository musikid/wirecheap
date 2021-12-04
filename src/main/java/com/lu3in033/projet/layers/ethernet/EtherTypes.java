package com.lu3in033.projet.layers.ethernet;

import java.util.Arrays;
import java.util.Optional;

public enum EtherTypes {
    IPv4(0x0800);

    private final int value;

    EtherTypes(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static Optional<String> valueNameFor(int t) {
        return Arrays.stream(EtherTypes.values()).filter(v -> v.value() == t).findFirst().map(EtherTypes::name);
    }
}
