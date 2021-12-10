package com.lu3in033.projet.layers.ethernet;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum EtherTypes {
    IPv4(0x0800);

    private final static Map<Integer, EtherTypes> VALUES = Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(v -> v.value, v -> v));
    public final int value;

    EtherTypes(int value) {
        this.value = value;
    }

    public static Optional<EtherTypes> get(int t) {
        return Optional.ofNullable(VALUES.get(t));
    }
}
