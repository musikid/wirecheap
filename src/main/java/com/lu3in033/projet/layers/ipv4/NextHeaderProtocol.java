package com.lu3in033.projet.layers.ipv4;

import java.util.Arrays;

public class NextHeaderProtocol {
    public final byte value;

    public NextHeaderProtocol(byte protocol) {
        value = protocol;
    }

    public String name() {
        return Arrays.stream(NextHeaderProtocols.values()).filter(v -> v.value() == value)
                .findFirst().map(NextHeaderProtocols::name).orElse("Unknown");
    }

    @Override
    public String toString() {
        return String.format("%d (%s)", Byte.toUnsignedInt(value), name());
    }
}
