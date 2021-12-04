package com.lu3in033.projet.layers.ipv4;

import com.lu3in033.projet.layers.Layer;
import com.lu3in033.projet.layers.LayerUtils;
import com.lu3in033.projet.layers.NotEnoughBytesException;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Ipv4 extends Layer {
    public static final int MIN_HEADER_LENGTH = 20;
    public final byte version;
    public final byte headerLength;
    // dscp + ecn
    public final TypeOfService typeOfService;
    public final short totalLength;
    public final short id;
    public final Ipv4Flags flags;
    public final short fragmentOffset;
    public final byte ttl;
    public final NextHeaderProtocol nextHeaderProtocol;
    public final short checksum;
    public final Ipv4Address source;
    public final Ipv4Address dest;
    public final List<Ipv4Option> options;

    public Ipv4(byte version, byte headerLength, TypeOfService typeOfService, short totalLength, short id, Ipv4Flags flags,
                short fragmentOffset, byte ttl, NextHeaderProtocol nextHeaderProtocol, short checksum, Ipv4Address source,
                Ipv4Address dest, List<Ipv4Option> options, List<Byte> payload) {
        super(payload);
        this.version = version;
        this.headerLength = headerLength;
        this.typeOfService = typeOfService;
        this.totalLength = totalLength;
        this.id = id;
        // We consider that only the three last bits are used
        this.flags = flags;
        this.fragmentOffset = fragmentOffset;
        this.ttl = ttl;
        this.nextHeaderProtocol = nextHeaderProtocol;
        this.checksum = checksum;
        this.source = source;
        this.dest = dest;
        this.options = options;
    }

    public static Ipv4 create(List<Byte> bytes) throws NotEnoughBytesException {
        if (bytes.size() < MIN_HEADER_LENGTH) {
            throw new NotEnoughBytesException(MIN_HEADER_LENGTH, bytes.size());
        }

        byte version = (byte) (bytes.get(0) >> 4);
        byte headerLength = (byte) (bytes.get(0) & 0xF);
        TypeOfService typeOfService = new TypeOfService(bytes.get(1));
        short totalLength = LayerUtils.getShort(bytes, 2);
        short id = LayerUtils.getShort(bytes, 4);

        Ipv4Flags flags = new Ipv4Flags((byte) (bytes.get(6) >> 5));
        short fragmentOffset = (short) ((bytes.get(6) & 0b00011111) << 8 | bytes.get(7));

        byte ttl = bytes.get(8);

        NextHeaderProtocol protocol = new NextHeaderProtocol(bytes.get(9));
        short checksum = LayerUtils.getShort(bytes, 10);

        Ipv4Address source = Ipv4Address.create(bytes.subList(12, 16));
        Ipv4Address dest = Ipv4Address.create(bytes.subList(16, 20));

        //TODO: We don't parse options for now
        return new Ipv4(version, headerLength, typeOfService, totalLength,
                id, flags, fragmentOffset, ttl, protocol, checksum, source, dest,
                new ArrayList<>(), bytes.subList((headerLength * 4) - 1, bytes.size()));
    }

    /**
     * Returns the header length in bytes.
     *
     * @return int
     */
    public int headerLength() {
        return headerLength * 4;
    }

    @Override
    public String toString() {
        return new StringJoiner("\n -> ", Ipv4.class.getSimpleName() + "\n -> ", "\n")
                .add("Version: " + version)
                .add("Header length: " + headerLength)
                .add("Type of service: " + typeOfService)
                .add("Total length: " + totalLength)
                .add("Id: " + id)
                .add("Flags: " + "\n    " + flags.toString().replaceAll("\n", "\n    "))
                .add("Fragment offset: " + fragmentOffset)
                .add("TTL: " + ttl)
                .add("Next header protocol: " + nextHeaderProtocol)
                .add("Checksum: " + String.format("0x%04x", Short.toUnsignedInt(checksum)))
                .add("Source: " + source)
                .add("Destination: " + dest)
                .add("Options: " + options)
                .toString();
    }
}
