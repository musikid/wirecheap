package com.lu3in033.projet.layers.dhcp;

import com.lu3in033.projet.layers.NotEnoughBytesException;
import com.lu3in033.projet.layers.ipv4.Ipv4Address;

import java.util.List;

public class Dhcp {
    public static final int MIN_PACKET_LENGTH = 312;

    public final OpCode code;
    public final HType htype;
    public final byte hlen;
    public final byte hops;
    public final int xid;
    public final short secs;
    public final DhcpFlags flags;
    public final Ipv4Address ciaddr;
    public final Ipv4Address yiaddr;
    public final Ipv4Address siaddr;
    public final Ipv4Address giaddr;
    public final Byte[] chaddr;
    public final String sname;
    public final String file;
    public final List<DhcpOption> options;

    public Dhcp(OpCode code, HType htype, byte hlen, byte hops, int xid, short secs, DhcpFlags flags,
                Ipv4Address ciaddr, Ipv4Address yiaddr, Ipv4Address siaddr, Ipv4Address giaddr,
                Byte[] chaddr, String sname, String file, List<DhcpOption> options) {
        this.code = code;
        this.htype = htype;
        this.hlen = hlen;
        this.hops = hops;
        this.xid = xid;
        this.secs = secs;
        this.flags = flags;
        this.ciaddr = ciaddr;
        this.yiaddr = yiaddr;
        this.siaddr = siaddr;
        this.giaddr = giaddr;
        this.chaddr = chaddr;
        this.sname = sname;
        this.file = file;
        this.options = options;
    }

    public static Dhcp create(List<Byte> bytes) throws NotEnoughBytesException {
        if (bytes.size() < MIN_PACKET_LENGTH) {
            throw new NotEnoughBytesException(MIN_PACKET_LENGTH, bytes.size());
        }

        return null;
    }
}
