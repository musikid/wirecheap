package com.projet.parser;

import java.util.List;

public class Frame {
    public final int id;
    public final List<Byte> buffer;

    public Frame(int id, List<Byte> buf) {
        this.id = id;
        buffer = buf;
    }
}