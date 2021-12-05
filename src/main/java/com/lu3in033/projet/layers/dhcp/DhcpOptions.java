package com.lu3in033.projet.layers.dhcp;

public enum DhcpOptions {
    LeaseTime(51), OptionOverload(52), MessageType(53);

    public int value;

    DhcpOptions(int op) {
        value = op;
    }
}
