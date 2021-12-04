package com.lu3in033.projet.layers.ipv4;

import com.lu3in033.projet.layers.NotEnoughBytesException;

import java.util.List;

public class Ipv4Address {
    public final Byte[] address;

    public Ipv4Address(Byte[] a) {
        assert (a.length == 4);
        address = a;
    }

    public static Ipv4Address create(List<Byte> bytes) throws NotEnoughBytesException {
        if (bytes.size() < 4) {
            throw new NotEnoughBytesException(4, bytes.size());
        }

        Byte[] b = new Byte[4];
        bytes.toArray(b);

        return new Ipv4Address(b);
    }

    @Override
    public String toString() {
        return String.format("%d.%d.%d.%d",
                address[0] & 0xFF,
                address[1] & 0xFF,
                address[2] & 0xFF,
                address[3] & 0xFF);
    }
}
