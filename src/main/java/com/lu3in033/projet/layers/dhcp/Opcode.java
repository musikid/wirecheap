package com.lu3in033.projet.layers.dhcp;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Opcode {
    private final int rawValue;

    public Opcode(byte v) {
        rawValue = v;
    }

    @Override
    public String toString() {
        return Optional.ofNullable(Opcodes.VALUES.get(rawValue))
                .map(Opcodes::name).orElse("Unknown");
    }

    enum Opcodes {
        Request(0),
        Reply(1);

        public static final Map<Integer, Opcodes> VALUES = Arrays.stream(Opcodes.values())
                .collect(Collectors.toUnmodifiableMap(o -> o.value, o -> o));

        public int value;

        Opcodes(int v) {
            value = v;
        }
    }
}
