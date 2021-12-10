package com.lu3in033.projet.parser.combinators;

public class Location {
    public final int line;
    public final int column;
    public final int pos;

    public Location(int l, int c, int pos) {
        line = l;
        column = c;
        this.pos = pos;
    }
}
