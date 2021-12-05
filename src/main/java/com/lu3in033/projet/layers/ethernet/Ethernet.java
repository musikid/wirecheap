package com.lu3in033.projet.layers.ethernet;

import com.lu3in033.projet.layers.Layer;
import com.lu3in033.projet.layers.NotEnoughBytesException;

import java.nio.ByteBuffer;
import java.util.StringJoiner;

public class Ethernet extends Layer {
    public static int HEADER_LENGTH = 14;
    public final MacAddress dest;
    public final MacAddress src;
    public final EtherType type;

    public Ethernet(MacAddress d, MacAddress s, EtherType t, ByteBuffer p) {
        super(p);
        dest = d;
        src = s;
        type = t;
    }

    public static Ethernet create(ByteBuffer frame) throws NotEnoughBytesException {
        if (frame.remaining() < HEADER_LENGTH) {
            throw new NotEnoughBytesException(HEADER_LENGTH, frame.remaining());
        }

        MacAddress dest = MacAddress.create(frame);
        MacAddress src = MacAddress.create(frame);
        short rawType = frame.getShort();
        EtherType type = new EtherType(rawType);
        ByteBuffer payload = frame.slice();

        return new Ethernet(dest, src, type, payload);
    }

    @Override
    public String toString() {
        return new StringJoiner("\n -> ", Ethernet.class.getSimpleName() + "\n -> ", "\n")
                .add("Source: " + src)
                .add("Destination: " + dest)
                .add(String.format("Type: 0x%04x (%s)", type.value(), type.name()))
                .toString();
    }
}