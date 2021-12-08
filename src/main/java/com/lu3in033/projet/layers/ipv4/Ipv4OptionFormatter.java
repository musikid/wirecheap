package com.lu3in033.projet.layers.ipv4;

import java.nio.ByteBuffer;
import java.util.function.Function;

interface Ipv4OptionFormatter extends Function<ByteBuffer, String> {
    Ipv4OptionFormatter EMPTY = byteBuffer -> "None";
    Ipv4OptionFormatter RAW_HEX = byteBuffer -> Integer.toUnsignedString(byteBuffer.getInt());
}
