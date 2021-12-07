package com.lu3in033.projet.layers.ipv4;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.StringJoiner;


/**
 * An instance of IPv4 option.
 * The bytes are interpreted when needed (with toString() for example).
 */
// Because enum can't have runtime data in Java,
// we only take the bytes and interpret them when needed (with toString() for example)
public class Ipv4Option {
    public int type;
    public final String name;
    public final Ipv4OptionFormatter formatter;
    public int length;
    public ByteBuffer data;

    public Ipv4Option(int type, int length, ByteBuffer data) {
        this.type = type;
        this.length = length;
        this.data = data;
        Optional<Ipv4Options> optType;
        if ((optType = Ipv4Options.get(type)).isPresent()) {
            var opt = optType.get();
            name = opt.prettyName;
            formatter = opt.formatter;
        } else {
            name = "Unknown";
            formatter = Ipv4OptionFormatter.RAW_HEX;
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
