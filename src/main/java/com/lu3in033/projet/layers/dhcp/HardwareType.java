package com.lu3in033.projet.layers.dhcp;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HardwareType {
    public final int rawValue;
    public final String name;

    public HardwareType(byte v) {
        rawValue = v;
        name = Optional.ofNullable(HardwareTypes.VALUES.get(rawValue))
                .map(HardwareTypes::name).orElse("Unknown");
    }

    @Override
    public String toString() {
        return String.format("0x%02x (%s)", rawValue, name);
    }

    enum HardwareTypes {
        Ethernet(1);

        public static final Map<Integer, HardwareTypes> VALUES = Arrays.stream(HardwareTypes.values())
                .collect(Collectors.toUnmodifiableMap(o -> o.value, o -> o));

        public int value;

        HardwareTypes(int v) {
            value = v;
        }


    }
}
