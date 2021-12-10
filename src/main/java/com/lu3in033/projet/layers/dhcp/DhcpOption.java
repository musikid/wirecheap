package com.lu3in033.projet.layers.dhcp;

import java.nio.ByteBuffer;
import java.util.StringJoiner;

/**
 * An instance of DHCP option.
 * The bytes are interpreted when needed (with toString() for example).
 */
// Because enum can't have runtime data in Java,
// we only take the bytes and interpret them when needed (with toString() for example)
public class DhcpOption {
    public final int type;
    public final int length;
    public final ByteBuffer data;
    public final String name;
    public final DhcpOptionFormatter formatter;

    public DhcpOption(int type, int length, ByteBuffer data) {
        this.type = type;
        this.length = length;
        this.data = data;
        DhcpOptions opt = DhcpOptions.get(type).orElse(DhcpOptions.Fallback);
        name = opt.prettyName;
        formatter = opt.formatter;
    }

    @Override
    public String toString() {
        return new StringJoiner("\n      -> ", "Option\n      -> ", "")
                .add(String.format("Type: %d (%s)", type, name))
                .add("Length: " + length)
                .add("Data: " + formatter.apply(data.rewind()))
                .toString();
    }
}
