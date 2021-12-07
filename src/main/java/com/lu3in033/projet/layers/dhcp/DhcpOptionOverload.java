package com.lu3in033.projet.layers.dhcp;

public enum DhcpOptionOverload {
    OverloadSname(1),
    OverloadFile(2);

    public int value;

    DhcpOptionOverload(int v) {
        value = v;
    }
}