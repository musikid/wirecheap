package com.lu3in033.projet.layers.ethernet;

import com.lu3in033.projet.layers.NotEnoughBytesException;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.StringJoiner;

public class MacAddress {
    public final byte[] buffer;

    public MacAddress(byte[] buf) {
        assert (buf != null);
        assert buf.length == 6;
        buffer = buf;
    }

    public static MacAddress create(ByteBuffer b) throws NotEnoughBytesException {
        if (b.remaining() < 6) {
            throw new NotEnoughBytesException(6, b.remaining());
        }

        byte[] buf = new byte[6];
        b.get(buf);

        return new MacAddress(buf);
    }

    @Override
    public String toString() {
        final StringJoiner s = new StringJoiner(":");
        for (byte b : buffer) {
            s.add(String.format("%02x", b));
        }
        return s.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MacAddress macAddr = (MacAddress) o;
        return Arrays.equals(buffer, macAddr.buffer);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(buffer);
    }
}
