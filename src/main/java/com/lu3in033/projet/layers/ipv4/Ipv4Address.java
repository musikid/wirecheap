package com.lu3in033.projet.layers.ipv4;

public class Ipv4Address {
    public final int address;

    public Ipv4Address(int a) {
        address = a;
    }

    @Override
    public String toString() {
        return String.format("%d.%d.%d.%d", address & 0xFF000000, address & 0x00FF0000, address & 0x0000FF00,
                address & 0x000000FF);
    }
}
