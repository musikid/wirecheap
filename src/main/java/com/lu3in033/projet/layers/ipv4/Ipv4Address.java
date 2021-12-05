package com.lu3in033.projet.layers.ipv4;

import com.lu3in033.projet.layers.NotEnoughBytesException;

import java.nio.ByteBuffer;

public class Ipv4Address {
    public final byte[] address;

    public Ipv4Address(byte[] a) {
        assert (a.length == 4);
        address = a;
    }

    public static Ipv4Address create(ByteBuffer bytes) throws NotEnoughBytesException {
        if (bytes.remaining() < 4) {
            throw new NotEnoughBytesException(4, bytes.remaining());
        }

        byte[] b = new byte[4];
        bytes.get(b);

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
