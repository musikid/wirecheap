package com.lu3in033.projet.layers.dhcp;

import com.lu3in033.projet.layers.NotEnoughBytesException;
import com.lu3in033.projet.layers.ethernet.MacAddress;
import com.lu3in033.projet.layers.ipv4.Ipv4Address;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringJoiner;
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
    public final ByteBuffer chaddr;
    public final String sname;
    public final String file;
    public final List<DhcpOption> options;

    public Dhcp(Opcode code, HardwareType htype, byte hlen, byte hops, int xid, short secs, DhcpFlags flags,
                Ipv4Address ciaddr, Ipv4Address yiaddr, Ipv4Address siaddr, Ipv4Address giaddr,
                ByteBuffer chaddr, String sname, String file, List<DhcpOption> options) {
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

        ByteBuffer chaddr = ByteBuffer.allocate(16);
        bytes.get(chaddr.array());

        ByteBuffer rawSname = ByteBuffer.allocate(64);
        bytes.get(rawSname.array());

        ByteBuffer rawFile = ByteBuffer.allocate(128);
        bytes.get(rawFile.array());

        int magicCookie = bytes.getInt();

        // Options magic happens here
        LinkedHashMap<Integer, DhcpOption> optionsMap = new LinkedHashMap<>();
        for (int type = bytes.get(); (type & 0xFF) != DhcpOptions.EndOfOptions.value
                && bytes.hasRemaining();
             type = bytes.get()) {
            addOption(optionsMap, type, bytes);
        }

        // We check if we also have options on sname and file
        DhcpOption optionOverload = optionsMap.get(DhcpOptions.OptionOverload.value);
        if (optionOverload != null) {
            byte value = optionOverload.data.rewind().get();
            if ((value & DhcpOptionOverload.OverloadSname.value) > 0) {
                loadOverloadedOptions(optionsMap, rawSname);
            }

            if ((value & DhcpOptionOverload.OverloadFile.value) > 0) {
                loadOverloadedOptions(optionsMap, rawFile);
            }

            List<DhcpOption> options = new ArrayList<>(optionsMap.values());

            return new Dhcp(op, htype, hlen, hops, xid, secs, flags, ciaddr, yiaddr, siaddr,
                    giaddr, chaddr, "", "", options);
        }

        // We use trim() to delete all null characters
        String sname = StandardCharsets.US_ASCII.decode(rawSname).toString().trim();
        // We use trim() to delete all null characters
        String file = StandardCharsets.US_ASCII.decode(rawFile).toString().trim();

        List<DhcpOption> options = new ArrayList<>(optionsMap.values());

        return new Dhcp(op, htype, hlen, hops, xid, secs, flags, ciaddr, yiaddr, siaddr,
                giaddr, chaddr, sname, file, options);
    }

    // With RFC3396, we have to reunite split options
    private static void addOption(LinkedHashMap<Integer, DhcpOption> options, int type, ByteBuffer bytes) {
        if (DhcpOptions.isFixed(type)) {
            options.put(type, new DhcpOption(type, 1, ByteBuffer.allocate(0)));
        } else {
            byte length = (byte) (bytes.get() & 0xFF);
            options.compute(type, (key, oldOption) -> {
                if (oldOption == null) {
                    ByteBuffer data = ByteBuffer.allocate(length);
                    bytes.get(data.array());
                    return new DhcpOption(key, length, data);
                }

                int oldLength = oldOption.data.array().length;
                int totalLength = oldLength + length;
                ByteBuffer newData = ByteBuffer.allocate(totalLength);
                newData.put(oldOption.data.array());
                bytes.get(newData.array(), oldLength, length);
                return new DhcpOption(key, totalLength, newData);
            });
        }
    }

    private static void loadOverloadedOptions(LinkedHashMap<Integer, DhcpOption> options, ByteBuffer bytes) {
        while (bytes.hasRemaining()) {
            int type = bytes.get();
            addOption(options, type, bytes);
        }
    }

    @Override
    public String toString() {
        String chaddrString;
        try {
            chaddrString = (htype.rawValue == HardwareType.HardwareTypes.Ethernet.value)
                    ? MacAddress.create(chaddr.rewind()).toString()
                    : DhcpOptionFormatter.RAW_HEX.apply(chaddr.rewind());
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
