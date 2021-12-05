package com.lu3in033.projet.layers.dhcp;

public class DhcpFlags {
    public final short flags;

    public DhcpFlags(short f) {
        flags = f;
    }

    @Override
    public String toString() {
        return "Broadcast: " + ((flags >>> 15) > 0 ? "Enabled" : "Disabled");
    }
}
