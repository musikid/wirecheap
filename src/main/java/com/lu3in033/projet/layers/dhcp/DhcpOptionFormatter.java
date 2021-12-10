package com.lu3in033.projet.layers.dhcp;

import com.lu3in033.projet.layers.NotEnoughBytesException;
import com.lu3in033.projet.layers.ipv4.Ipv4Address;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import java.util.function.Function;

interface DhcpOptionFormatter extends Function<ByteBuffer, String> {
    // We use trim() to delete all null characters
    DhcpOptionFormatter ASCII = byteBuffer -> StandardCharsets.US_ASCII.decode(byteBuffer).toString().trim();
    DhcpOptionFormatter EMPTY = byteBuffer -> "None";
    DhcpOptionFormatter UNSIGNED_INTEGER = byteBuffer -> Integer.toUnsignedString(byteBuffer.getInt());
    DhcpOptionFormatter UNSIGNED_SHORT = byteBuffer -> Integer.toUnsignedString(byteBuffer.getShort());
    DhcpOptionFormatter UNSIGNED_BYTE = byteBuffer -> Integer.toUnsignedString(Byte.toUnsignedInt(byteBuffer.get()));
    DhcpOptionFormatter INTEGER = byteBuffer -> Integer.toString(byteBuffer.getInt());
    DhcpOptionFormatter RAW_HEX = byteBuffer -> {
        StringJoiner s = new StringJoiner(", ", "[", "]");
        while (byteBuffer.hasRemaining())
            s.add(String.format("0x%02x", byteBuffer.get()));
        return s.toString();
    };
    DhcpOptionFormatter BOOLEAN = b -> b.get() == 1 ? "Enabled" : "Disabled";
    DhcpOptionFormatter IP = byteBuffer -> {
        try {
            return Ipv4Address.create(byteBuffer).toString();
        } catch (NotEnoughBytesException e) {
            e.printStackTrace();
            return null;
        }
    };
    DhcpOptionFormatter IP_LIST = byteBuffer -> {
        // TODO: Change delimiter
        StringJoiner s = new StringJoiner(", ");
        int ipCount = byteBuffer.remaining() / 4;
        for (int i = 0; i < ipCount; i++) {
            s.add(IP.apply(byteBuffer));
        }
        return s.toString();
    };
}
