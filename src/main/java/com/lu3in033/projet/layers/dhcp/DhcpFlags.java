package com.lu3in033.projet.layers.dhcp;

public class DhcpFlags {
    public final short flags;

    public DhcpFlags(short f) {
        flags = f;
    }

    @Override
    public String toString() {
        return "Broadcast: " + ((flags & 0x8000) > 0 ? "Enabled" : "Disabled");
    }
}
