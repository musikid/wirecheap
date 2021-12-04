package com.lu3in033.projet.layers.udp;

import com.lu3in033.projet.layers.Layer;
import com.lu3in033.projet.layers.LayerUtils;
import com.lu3in033.projet.layers.NotEnoughBytesException;

import java.util.List;
import java.util.StringJoiner;

public class Udp extends Layer {
    public static int HEADER_LENGTH = 8;
    public short destPort;
    public short srcPort;
    public short length;
    public short checksum;

    public Udp(short destPort, short srcPort, short length, short checksum, List<Byte> payload) {
        super(payload);
        this.destPort = destPort;
        this.srcPort = srcPort;
        this.length = length;
        this.checksum = checksum;
    }

    public static Udp create(List<Byte> bytes) throws NotEnoughBytesException {
        if (bytes.size() < HEADER_LENGTH) {
            throw new NotEnoughBytesException(HEADER_LENGTH, bytes.size());
        }

        short destPort = LayerUtils.getShort(bytes, 0);
        short srcPort = LayerUtils.getShort(bytes, 2);
        short length = LayerUtils.getShort(bytes, 4);
        short checksum = LayerUtils.getShort(bytes, 6);
        List<Byte> payload = bytes.subList(8, bytes.size());

        return new Udp(destPort, srcPort, length, checksum, payload);
    }

    @Override
    public String toString() {
        return new StringJoiner("\n -> ", Udp.class.getSimpleName() + "\n -> ", "\n")
                .add("Destination port: " + Short.toUnsignedInt(destPort))
                .add("Source port: " + Short.toUnsignedInt(srcPort))
                .add("Length: " + Short.toUnsignedInt(length))
                .add("Checksum: " + String.format("0x%04x", Short.toUnsignedInt(checksum)))
                .toString();
    }
}