package com.lu3in033.projet.layers.udp;

import com.lu3in033.projet.layers.Layer;

import java.util.List;

public class Udp extends Layer {
    public short destPort;
    public short srcPort;
    public short length;
    public short checksum;

    public Udp(short destPort,
               short srcPort,
               short length,
               short checksum, List<Byte> payload) {
        super(payload);
        this.destPort = destPort;
        this.srcPort = srcPort;
        this.length = length;
        this.checksum = checksum;
    }
}