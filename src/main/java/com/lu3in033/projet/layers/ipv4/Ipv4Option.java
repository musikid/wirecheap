package com.lu3in033.projet.layers.ipv4;

import java.util.List;

public class Ipv4Option {
    public int type;
    public List<Byte> data;

    public Ipv4Option(int type, int length, List<Byte> data) {
        this.type = type;
        this.data = data;
    }

    public int length() {
        return this.data.size();
    }
}
