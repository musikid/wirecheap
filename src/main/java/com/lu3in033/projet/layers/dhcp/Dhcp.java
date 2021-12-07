package com.lu3in033.projet.layers.dhcp;

import com.lu3in033.projet.layers.NotEnoughBytesException;
import com.lu3in033.projet.layers.ethernet.MacAddress;
import com.lu3in033.projet.layers.ipv4.Ipv4Address;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Dhcp {
    public static final int MIN_PACKET_LENGTH = 236;

    public final Opcode op;
    public final HardwareType htype;
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

    public Dhcp(Opcode code, HardwareType htype, byte hlen, byte hops, int xid, short secs, DhcpFlags flags,
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

        Opcode op = new Opcode(bytes.get());

        HardwareType htype = new HardwareType(bytes.get());
        byte hlen = bytes.get();
        byte hops = bytes.get();

        int xid = bytes.getInt();
        short secs = bytes.getShort();
        DhcpFlags flags = new DhcpFlags(bytes.getShort());

        Ipv4Address ciaddr = Ipv4Address.create(bytes);
        Ipv4Address yiaddr = Ipv4Address.create(bytes);
        Ipv4Address siaddr = Ipv4Address.create(bytes);
        Ipv4Address giaddr = Ipv4Address.create(bytes);

        byte[] chaddr = new byte[16];
        bytes.get(chaddr);

        ByteBuffer rawSname = ByteBuffer.allocate(64);
        bytes.get(rawSname.array());

        ByteBuffer rawFile = ByteBuffer.allocate(128);
        bytes.get(rawFile.array());

        int magicCookie = bytes.getInt();

        // Options magic happens here
        List<DhcpOption> options = new ArrayList<>();
        for (int type = bytes.get(); (type & 0xFF) != DhcpOptions.EndOfOptions.value
                && bytes.hasRemaining();
             type = bytes.get()) {
            DhcpOption option;
            if (DhcpOptions.isFixed(type)) {
                option = new DhcpOption(type, 1, ByteBuffer.allocate(0));
            } else {
                byte length = (byte) (bytes.get() & 0xFF);
                ByteBuffer data = ByteBuffer.allocate(length);
                bytes.get(data.array());
                option = new DhcpOption(type, length, data);
            }

            options.add(option);
        }

        // We check if we also have options on sname and file
        Optional<DhcpOption> optionOverload = options.stream()
                .filter(opt -> opt.type == DhcpOptions.OptionOverload.value).findFirst();
        if (optionOverload.isPresent()) {
            byte value = optionOverload.get().data.get();
            if ((value & DhcpOptionOverload.OverloadSname.value) > 0) {
                loadOverloadedOptions(options, rawSname);
            }

            if ((value & DhcpOptionOverload.OverloadFile.value) > 0) {
                loadOverloadedOptions(options, rawFile);
            }

            return new Dhcp(op, htype, hlen, hops, xid, secs, flags, ciaddr, yiaddr, siaddr,
                    giaddr, chaddr, "", "", options);
        }

        // We use trim() to delete all null characters
        String sname = StandardCharsets.US_ASCII.decode(rawSname).toString().trim();
        // We use trim() to delete all null characters
        String file = StandardCharsets.US_ASCII.decode(rawFile).toString().trim();

        return new Dhcp(op, htype, hlen, hops, xid, secs, flags, ciaddr, yiaddr, siaddr,
                giaddr, chaddr, sname, file, options);
    }

    private static DhcpOption getOption(int type, ByteBuffer bytes) {
        DhcpOption option;
        if (DhcpOptions.isFixed(type)) {
            option = new DhcpOption(type, 1, ByteBuffer.allocate(0));
        } else {
            byte length = (byte) (bytes.get() & 0xFF);
            ByteBuffer data = ByteBuffer.allocate(length);
            bytes.get(data.array());
            option = new DhcpOption(type, length, data);
        }

        return option;
    }

    private static void loadOverloadedOptions(List<DhcpOption> options, ByteBuffer bytes) {
        while (bytes.hasRemaining()) {
            int type = bytes.get();
            options.add(getOption(type, bytes));
        }
    }

    @Override
    public String toString() {
        String chaddrString;
        try {
            chaddrString = (htype.rawValue == HardwareType.HardwareTypes.Ethernet.value)
                    ? MacAddress.create(ByteBuffer.wrap(chaddr).slice(1, 6)).toString()
                    : Arrays.toString(chaddr);
        } catch (NotEnoughBytesException e) {
            e.printStackTrace();
            return null;
        }
        String optionsStr = options.stream().map(DhcpOption::toString).collect(Collectors.joining("\n   -> "));

        return new StringJoiner("\n -> ", "DHCP\n -> ", "\n")
                .add("Message type: " + op)
                .add("Hardware type: " + htype)
                .add("Hardware address length: " + hlen)
                .add("Hops: " + Byte.toUnsignedInt(hops))
                .add("Transaction ID: " + Integer.toUnsignedString(xid))
                .add("Seconds elapsed: " + Integer.toUnsignedString(secs))
                .add("Flags: " + flags)
                .add("Client IP address: " + ciaddr)
                .add("Your IP address: " + yiaddr)
                .add("Next IP address: " + siaddr)
                .add("Relay agent IP address: " + giaddr)
                .add("Client Hardware address: " + chaddrString)
                .add("Server host name: " + sname)
                .add("Boot file name: " + file)
                .add("Options: " + optionsStr)
                .toString();
    }
}
