package com.lu3in033.projet.layers.ethernet;

import com.lu3in033.projet.layers.Layer;
import com.lu3in033.projet.layers.NotEnoughBytesException;

import java.util.List;

public class Ethernet extends Layer {
    public static int HEADER_LENGTH = 14;
    public final MacAddress dest;
    public final MacAddress src;
    public final EtherType type;

    public Ethernet(MacAddress d, MacAddress s, EtherType t, List<Byte> p) {
        super(p);
        dest = d;
        src = s;
        type = t;
    }

    public static Ethernet create(List<Byte> frame) throws NotEnoughBytesException {
        if (frame.size() < HEADER_LENGTH) {
            throw new NotEnoughBytesException(HEADER_LENGTH, frame.size());
        }

        MacAddress src = MacAddress.create(frame.subList(0, 6));
        MacAddress dest = MacAddress.create(frame.subList(6, 12));
        short rawType = (short) (frame.get(12) << 8 | frame.get(13));
        EtherType type = new EtherType(rawType);
        List<Byte> payload = frame.subList(14, frame.size());

        return new Ethernet(dest, src, type, payload);
    }
}