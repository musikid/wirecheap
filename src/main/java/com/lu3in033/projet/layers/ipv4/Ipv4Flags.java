package com.lu3in033.projet.layers.ipv4;

import java.util.StringJoiner;

public class Ipv4Flags {
    public byte rawValue;
    public boolean unused;
    public boolean dontFragment;
    public boolean moreFragments;

    public Ipv4Flags(byte rawValue, boolean unused, boolean dontFragment, boolean moreFragments) {
        this.rawValue = rawValue;
        this.unused = unused;
        this.dontFragment = dontFragment;
        this.moreFragments = moreFragments;
    }

    public Ipv4Flags(byte flags) {
        this(flags, (flags & 0b100) == 0b100, (flags & 0b10) == 0b10, (flags & 0b1) == 0b1);
    }

    @Override
    public String toString() {
        return new StringJoiner("\n")
                .add("Reserved: " + unused)
                .add("Don't Fragment: " + dontFragment)
                .add("More Fragments: " + moreFragments).toString();
    }
}
