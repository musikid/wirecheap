package com.lu3in033.projet.layers.dhcp;

import java.util.Arrays;
import java.util.Optional;

public enum DhcpMessageTypes {
    Discover(1),
    Offer(2),
    Request(3),
    Decline(4),
    ACK(5),
    NAK(6),
    Release(7),
    Inform(8);

    public int value;

    DhcpMessageTypes(int v) {
        value = v;
    }

    public static Optional<DhcpMessageTypes> valueFor(int v) {
        return Arrays.stream(DhcpMessageTypes.values()).filter(t -> t.value == v).findFirst();
    }
}
