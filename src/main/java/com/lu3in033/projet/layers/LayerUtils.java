package com.lu3in033.projet.layers;

import java.util.List;

public final class LayerUtils {
    private LayerUtils() {
    }

    public static short getShort(List<Byte> bytes, int i) {
        return (short) (bytes.get(i) << 8 | bytes.get(i + 1) & 0xFF);
    }
}
