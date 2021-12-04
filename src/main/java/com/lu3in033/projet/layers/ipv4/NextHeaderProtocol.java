package com.lu3in033.projet.layers.ipv4;

import java.util.Arrays;

public class NextHeaderProtocol {
    public final byte protocol;

    public NextHeaderProtocol(byte protocol) {
        this.protocol = protocol;
    }

    public String name() {
        return Arrays.stream(NextHeaderProtocols.values()).filter(v -> v.value() == protocol)
                .findFirst().map(NextHeaderProtocols::name).orElse("Unknown");
    }

    @Override
    public String toString() {
        return String.format("0x%02x (%s)", protocol, name());
    }
}
