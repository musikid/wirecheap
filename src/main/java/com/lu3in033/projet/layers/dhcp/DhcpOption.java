package com.lu3in033.projet.layers.dhcp;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.StringJoiner;

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
        Optional<DhcpOptions> optType;
        if ((optType = DhcpOptions.get(type)).isPresent()) {
            var opt = optType.get();
            name = opt.prettyName;
            formatter = opt.formatter;
        } else {
            name = "Unknown";
            formatter = DhcpOptionFormatter.RAW_HEX;
        }
    }

    @Override
    public String toString() {
        String s = new StringJoiner("\n      -> ", "Option\n      -> ", "")
                .add(String.format("Type: %d (%s)", type, name))
                .add("Length: " + length)
                .add("Data: " + formatter.apply(data))
                .toString();
        data.rewind();
        return s;
    }
}
