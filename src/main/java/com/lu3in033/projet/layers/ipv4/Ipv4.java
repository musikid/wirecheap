package com.lu3in033.projet.layers.ipv4;

import com.lu3in033.projet.layers.Layer;
import com.lu3in033.projet.layers.NotEnoughBytesException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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
                Ipv4Address dest, List<Ipv4Option> options, ByteBuffer payload) {
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

    public static Ipv4 create(ByteBuffer bytes) throws NotEnoughBytesException {
        if (bytes.remaining() < MIN_HEADER_LENGTH) {
            throw new NotEnoughBytesException(MIN_HEADER_LENGTH, bytes.remaining());
        }

        byte vhByte = bytes.get();
        byte version = (byte) (vhByte >> 4);
        byte headerLength = (byte) (vhByte & 0xF);

        TypeOfService typeOfService = new TypeOfService(bytes.get());
        short totalLength = bytes.getShort();
        short id = bytes.getShort();

        byte flagsFragByte = bytes.get();
        Ipv4Flags flags = new Ipv4Flags((byte) (flagsFragByte >> 5));
        short fragmentOffset = (short) ((flagsFragByte & 0x1F) << 8 | bytes.get());

        byte ttl = bytes.get();

        NextHeaderProtocol protocol = new NextHeaderProtocol(bytes.get());
        short checksum = bytes.getShort();

        Ipv4Address source = Ipv4Address.create(bytes);
        Ipv4Address dest = Ipv4Address.create(bytes);

        List<Ipv4Option> options = new ArrayList<>();
        // If we have options
        if (headerLength * 4 > MIN_HEADER_LENGTH) {
            for (int type = bytes.get(); type != Ipv4Options.EOOL.type;
                 type = bytes.get()) {
                Ipv4Option option;
                if (Ipv4Options.isFixed(type)) {
                    option = new Ipv4Option(type, 1, ByteBuffer.allocateDirect(0));
                } else {
                    int length = bytes.get();
                    ByteBuffer data = ByteBuffer.allocate(length);
                    bytes.get(data.array());
                    option = new Ipv4Option(type, length, data);
                }

                options.add(option);
            }
        }

        return new Ipv4(version, headerLength, typeOfService, totalLength,
                id, flags, fragmentOffset, ttl, protocol, checksum, source, dest,
                options, bytes.slice());
    }

    @Override
    public String toString() {
        String optionsStr = options.stream().map(Ipv4Option::toString).collect(Collectors.joining("\n   -> "));

        return new StringJoiner("\n -> ", "IPv4\n -> ", "\n")
                .add("Version: " + version)
                .add(String.format("Header length: %d (%d bytes)", headerLength, headerLength * 4))
                .add("Type of service: " + typeOfService)
                .add(String.format("Total length: %d bytes", Short.toUnsignedInt(totalLength)))
                .add("ID: " + String.format("0x%04x", Short.toUnsignedInt(id)))
                .add("Flags: " + "\n    " + flags.toString().replaceAll("\n", "\n    "))
                .add("Fragment offset: " + fragmentOffset)
                .add("TTL: " + Byte.toUnsignedInt(ttl))
                .add("Next header protocol: " + nextHeaderProtocol)
                .add("Checksum: " + String.format("0x%04x", Short.toUnsignedInt(checksum)))
                .add("Source: " + source)
                .add("Destination: " + dest)
                .add("Options: " + optionsStr)
                .toString();
    }
}
