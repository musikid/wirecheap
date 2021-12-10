package com.lu3in033.projet.layers.ipv4;

import com.lu3in033.projet.layers.NotEnoughBytesException;

import java.nio.ByteBuffer;
import java.util.StringJoiner;
import java.util.function.Function;

interface Ipv4OptionFormatter extends Function<ByteBuffer, String> {
    Ipv4OptionFormatter EMPTY = byteBuffer -> "None";
    Ipv4OptionFormatter RAW_HEX = byteBuffer -> Integer.toUnsignedString(byteBuffer.getInt());
    Ipv4OptionFormatter IP = byteBuffer -> {
        try {
            return Ipv4Address.create(byteBuffer).toString();
        } catch (NotEnoughBytesException e) {
            e.printStackTrace();
            return null;
        }
    };
    Ipv4OptionFormatter IP_LIST = byteBuffer -> {
        StringJoiner s = new StringJoiner(", ");
        int ipCount = byteBuffer.remaining() / 4;
        for (int i = 0; i < ipCount; i++) {
            s.add(IP.apply(byteBuffer));
        }
        return s.toString();
    };
    Ipv4OptionFormatter ROUTE = buffer -> {
        int pointer = buffer.get();
        String ipList = IP_LIST.apply(buffer);
        return String.format("Pointer: %d, List: %s", pointer, ipList);
    };
}
