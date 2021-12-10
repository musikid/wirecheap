package com.lu3in033.projet.layers;

import java.nio.ByteBuffer;
import java.util.StringJoiner;

public abstract class Layer {
    private final ByteBuffer payload;

    protected Layer(ByteBuffer payload) {
        this.payload = payload;
    }

    public String payloadString() {
        StringJoiner s = new StringJoiner(", ", "[", "]");
        for (byte b : payload.array()) {
            s.add(String.format("0x%02x", b));
        }
        return s.toString();
    }
}
