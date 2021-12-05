package com.lu3in033.projet.layers;

import java.nio.ByteBuffer;

public abstract class Layer {
    private final ByteBuffer payload;

    protected Layer(ByteBuffer payload) {
        this.payload = payload;
    }

    public ByteBuffer payload() {
        return payload;
    }
}
