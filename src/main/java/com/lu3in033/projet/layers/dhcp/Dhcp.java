package com.lu3in033.projet.layers.dhcp;

import com.lu3in033.projet.layers.NotEnoughBytesException;
import com.lu3in033.projet.layers.ipv4.Ipv4Address;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class Dhcp {
    public static final int MIN_PACKET_LENGTH = 312;

    public final byte op;
    public final byte htype;
    public final byte hlen;
    public final byte hops;
    public final int xid;
    public final short secs;
    public final DhcpFlags flags;
    public final Ipv4Address ciaddr;
    public final Ipv4Address yiaddr;
    public final Ipv4Address siaddr;
    public final Ipv4Address giaddr;
    public final byte[] chaddr;
    public final String sname;
    public final String file;
    public final List<DhcpOption> options;

    public Dhcp(byte code, byte htype, byte hlen, byte hops, int xid, short secs, DhcpFlags flags,
                Ipv4Address ciaddr, Ipv4Address yiaddr, Ipv4Address siaddr, Ipv4Address giaddr,
                byte[] chaddr, String sname, String file, List<DhcpOption> options) {
        this.op = code;
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

    public static Dhcp create(ByteBuffer bytes) throws NotEnoughBytesException {
        if (bytes.remaining() < MIN_PACKET_LENGTH) {
            throw new NotEnoughBytesException(MIN_PACKET_LENGTH, bytes.remaining());
        }

        byte op = bytes.get();
        byte htype = bytes.get();
        byte hlen = bytes.get();
        byte hops = bytes.get();
        int xid = bytes.getInt();
        short secs = bytes.getShort();
        short flags = bytes.getShort();

        Ipv4Address ciaddr = Ipv4Address.create(bytes);
        Ipv4Address yiaddr = Ipv4Address.create(bytes);
        Ipv4Address siaddr = Ipv4Address.create(bytes);
        Ipv4Address giaddr = Ipv4Address.create(bytes);

        byte[] rawChaddr = new byte[16];
        bytes.get(rawChaddr);

        byte[] rawSname = new byte[64];
        bytes.get(rawSname);
        String sname = StandardCharsets.US_ASCII.decode(ByteBuffer.wrap(rawSname)).toString();

        byte[] rawFile = new byte[128];
        bytes.get(rawFile);
        String file = StandardCharsets.US_ASCII.decode(ByteBuffer.wrap(rawFile)).toString();

        int magicCookie = bytes.getInt();
        // Options magic happens here
        List<DhcpOption> options = new ArrayList<>();
        int type = bytes.get();
        do {
            DhcpOption option;
            if (DhcpOptions.FIXED_LENGTH.contains(type)) {
                option = new DhcpOption(type, (byte) 1, ByteBuffer.allocate(0));
            } else {
                byte length = bytes.get();
                ByteBuffer data = ByteBuffer.allocate(length);
                bytes.put(data.array());
                option = new DhcpOption(type, length, data);
            }

            options.add(option);
        } while ((type & 0xFF) != DhcpOptions.EndOfOptions.value);

        return null;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Dhcp.class.getSimpleName() + "[", "]")
                .add("op=" + op)
                .add("htype=" + htype)
                .add("hlen=" + hlen)
                .add("hops=" + hops)
                .add("xid=" + xid)
                .add("secs=" + secs)
                .add("flags=" + flags)
                .add("ciaddr=" + ciaddr)
                .add("yiaddr=" + yiaddr)
                .add("siaddr=" + siaddr)
                .add("giaddr=" + giaddr)
                .add("chaddr=" + Arrays.toString(chaddr))
                .add("sname='" + sname + "'")
                .add("file='" + file + "'")
                .add("options=" + options)
                .toString();
    }
}
