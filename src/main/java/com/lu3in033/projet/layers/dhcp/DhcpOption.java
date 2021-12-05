package com.lu3in033.projet.layers.dhcp;

import java.nio.ByteBuffer;
import java.util.StringJoiner;

public class DhcpOption {
    public final int type;
    public final byte length;
    public final ByteBuffer data;

    public DhcpOption(int type, byte length, ByteBuffer data) {
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
