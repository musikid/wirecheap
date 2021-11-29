package com.lu3in033.projet.layers;

import java.util.List;

public abstract class Layer {
    private final List<Byte> payload;

    protected Layer(List<Byte> payload) {
        this.payload = payload;
    }

    public List<Byte> payload() {
        return payload;
    }
}
