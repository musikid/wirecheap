package com.projet.parser;

import java.util.List;

public class Fragment {
    private final int offset;
    private final List<Byte> buffer;

    public Fragment(int off, List<Byte> buf) {
        offset = off;
        buffer = buf;
    }

    public static List<Byte> mergeFragments(List<Fragment> fragments) {
        return null;
    }
}
