package com.lu3in033.projet.layers.dhcp;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum DhcpMessageTypes {
    Discover(1),
    Offer(2),
    Request(3),
    Decline(4),
    ACK(5),
    NAK(6),
    Release(7),
    Inform(8);

    public final static Map<Integer, DhcpMessageTypes> VALUES = Arrays.stream(DhcpMessageTypes.values())
            .collect(Collectors.toUnmodifiableMap(t -> t.value, t -> t));

    public int value;

    DhcpMessageTypes(int v) {
        value = v;
    }

    public static Optional<DhcpMessageTypes> get(int v) {
        return Optional.ofNullable(VALUES.get(v));
    }
}
