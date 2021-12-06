package com.lu3in033.projet.layers.dhcp;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Opcode {
    private final int rawValue;
    private final String name;

    public Opcode(byte v) {
        rawValue = v;
        name = Optional.ofNullable(Opcodes.VALUES.get(rawValue))
                .map(Opcodes::name).orElse("Unknown");
    }

    @Override
    public String toString() {
        return String.format("0x%02x (%s)", rawValue, name);
    }

    enum Opcodes {
        Request(1),
        Reply(2);

        public static final Map<Integer, Opcodes> VALUES = Arrays.stream(Opcodes.values())
                .collect(Collectors.toUnmodifiableMap(o -> o.value, o -> o));

        public int value;

        Opcodes(int v) {
            value = v;
        }
    }
}
