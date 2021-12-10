package com.lu3in033.projet.layers.udp;

import com.lu3in033.projet.layers.Layer;
import com.lu3in033.projet.layers.NotEnoughBytesException;

import java.nio.ByteBuffer;
import java.util.StringJoiner;

public class Udp extends Layer {
    public static int HEADER_LENGTH = 8;
    public short destPort;
    public short srcPort;
    public short length;
    public short checksum;

    public Udp(short destPort, short srcPort, short length, short checksum, ByteBuffer payload) {
        super(payload);
        this.destPort = destPort;
        this.srcPort = srcPort;
        this.length = length;
        this.checksum = checksum;
    }

    public static Udp create(ByteBuffer bytes) throws NotEnoughBytesException {
        if (bytes.remaining() < HEADER_LENGTH) {
            throw new NotEnoughBytesException(HEADER_LENGTH, bytes.remaining());
        }

        short destPort = bytes.getShort();
        short srcPort = bytes.getShort();
        short length = bytes.getShort();
        short checksum = bytes.getShort();
        ByteBuffer payload = bytes.slice();

        return new Udp(destPort, srcPort, length, checksum, payload);
    }

    @Override
    public String toString() {
        return new StringJoiner("\n\t-> ", "UDP\n\t-> ", "\n")
                .add("Destination port: " + Short.toUnsignedInt(destPort))
                .add("Source port: " + Short.toUnsignedInt(srcPort))
                .add("Length: " + Short.toUnsignedInt(length))
                .add("Checksum: " + String.format("0x%04x", Short.toUnsignedInt(checksum)))
                .toString();
    }
}