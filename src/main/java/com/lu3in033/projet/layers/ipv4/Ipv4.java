package com.lu3in033.projet.layers.ipv4;

import com.lu3in033.projet.layers.Layer;

import java.util.List;
import java.util.StringJoiner;

public class Ipv4 extends Layer {
    public final byte version;
    public final byte headerLength;
    // dscp + ecn
    public final byte typeOfService;
    public final short totalLength;
    public final short id;
    public final byte flags;
    public final short fragmentOffset;
    public final byte ttl;
    public final NextHeaderProtocol nextHeaderProtocol;
    public final short checksum;
    public final Ipv4Address source;
    public final Ipv4Address dest;
    public final List<Ipv4Option> options;

    public Ipv4(byte version, byte headerLength, byte typeOfService, short totalLength, short id, byte flags,
                short fragmentOffset, byte ttl, byte nextHeaderProtocol, short checksum, Ipv4Address source,
                Ipv4Address dest, List<Ipv4Option> options, List<Byte> payload) {
        super(payload);
        this.version = version;
        this.headerLength = headerLength;
        this.typeOfService = typeOfService;
        this.totalLength = totalLength;
        this.id = id;
        this.flags = flags;
        this.fragmentOffset = fragmentOffset;
        this.ttl = ttl;
        this.nextHeaderProtocol = new NextHeaderProtocol(nextHeaderProtocol);
        this.checksum = checksum;
        this.source = source;
        this.dest = dest;
        this.options = options;
    }

    @Override
    public String toString() {
        return new StringJoiner("\n ➔", "", "]").add("Version: " + version)
                .add("Header length: " + headerLength).add("Type of service: " + typeOfService)
                .add("Total length: " + totalLength).add("Id: " + id).add("Flags: " + flags)
                .add("Fragment offset: " + fragmentOffset).add("TTL: " + ttl)
                .add("Next header protocol: " + nextHeaderProtocol).add("Checksum: " + checksum)
                .add("Source: " + source).add("Destination: " + dest).add("Options: " + options).toString();
    }
}
