package com.lu3in033.projet.layers.ethernet;

public class EtherType {
    public final short type;
    public final String name;


    EtherType(short t) {
        type = t;
        name = EtherTypes.get(t).map(EtherTypes::name).orElse("Unknown");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EtherType etherType = (EtherType) o;

        return type == etherType.type;
    }

    @Override
    public int hashCode() {
        return type;
    }
}
