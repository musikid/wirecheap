package com.projet.parser;

import java.util.ArrayList;

public class Fragment {
    private final int offset;
    private final ArrayList<Byte> buffer;

    public Fragment(int off, ArrayList<Byte> buf) {
        offset = off;
        buffer = buf;
    }

    public static ArrayList<Byte> mergeFragments(ArrayList<Fragment> fragments) {
        return null;
    }
}
