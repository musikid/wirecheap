package com.lu3in033.projet.layers.dhcp;

import java.util.List;
import java.util.StringJoiner;

public class DhcpOption {
    public final DhcpOptionType type;
    public final byte length;
    public final List<Byte> data;

    public DhcpOption(DhcpOptionType type, byte length, List<Byte> data) {
        this.type = type;
        this.length = length;
        this.data = data;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DhcpOption.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("length=" + length)
                .add("data=" + data)
                .toString();
    }
}
