package com.lu3in033.projet.layers.ipv4;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public enum Ipv4Options {
    EOOL(0, "End of Options", Ipv4OptionFormatter.EMPTY),
    NOP(1, "No Operation", Ipv4OptionFormatter.EMPTY);

    private final static Set<Integer> FIXED_LENGTH = Set.of(EOOL.type, NOP.type);
    private final static Map<Integer, Ipv4Options> VALUES = Arrays.stream(Ipv4Options.values())
            .collect(Collectors.toUnmodifiableMap(t -> t.type, t -> t));

    public int type;
    public String prettyName;
    public Ipv4OptionFormatter formatter;

    Ipv4Options(int t, String s, Ipv4OptionFormatter f) {
        type = t;
        prettyName = s;
        formatter = f;
    }

    public static boolean isFixed(int t) {
        return FIXED_LENGTH.contains(t);
    }

    public static Optional<Ipv4Options> get(int code) {
        return Optional.ofNullable(VALUES.get(code));
    }
}