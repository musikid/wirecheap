package com.lu3in033.projet.layers.dhcp;

import com.lu3in033.projet.layers.NotEnoughBytesException;
import com.lu3in033.projet.layers.ethernet.MacAddress;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Defined DHCP options.
 * We define the option code, pretty name and a String formatter.
 */
public enum DhcpOptions {
    Pad(0, "Padding", DhcpOptionFormatter.EMPTY),
    SubnetMask(1, "Subnet Mask", DhcpOptionFormatter.IP),
    TimeOffset(2, "Time Offset", DhcpOptionFormatter.INTEGER),
    Router(3, "Router", DhcpOptionFormatter.IP_LIST),
    TimeServer(4, "Time Server", DhcpOptionFormatter.IP_LIST),
    NameServer(5, "Name Server", DhcpOptionFormatter.IP_LIST),
    DNS(6, "Domain Name Server", DhcpOptionFormatter.IP_LIST),
    LogServer(7, "Log Server", DhcpOptionFormatter.IP_LIST),
    CookieServer(8, "Cookie Server", DhcpOptionFormatter.IP_LIST),
    LPRServer(9, "LPR Server", DhcpOptionFormatter.IP_LIST),
    ImpressServer(10, "Impress Server", DhcpOptionFormatter.IP_LIST),
    ResourceServer(11, "Resource Location Server", DhcpOptionFormatter.IP_LIST),
    HostName(12, "Host Name", DhcpOptionFormatter.ASCII),
    BootSize(13, "Boot File Size", DhcpOptionFormatter.UNSIGNED_SHORT),
    MeritDumpFile(14, "Merit Dump File", DhcpOptionFormatter.ASCII),
    DomainName(15, "Domain Name", DhcpOptionFormatter.ASCII),
    Swap(16, "Swap Server", DhcpOptionFormatter.IP),
    RootPath(17, "Root Path", DhcpOptionFormatter.ASCII),
    ExtensionsPath(18, "Extensions Path", DhcpOptionFormatter.ASCII),
    IPForwarding(19, "IP Forwarding", DhcpOptionFormatter.BOOLEAN),
    NonLocal(20, "Non-Local Source Routing", DhcpOptionFormatter.BOOLEAN),
    Broadcast(28, "Broadcast Address", DhcpOptionFormatter.IP),
    RequestedIp(50, "Requested IP Address", DhcpOptionFormatter.IP),
    LeaseTime(51, "IP Lease Time", DhcpOptionFormatter.UNSIGNED_INTEGER),
    //TODO: Option overload
    OptionOverload(52, "Option Overload",
            byteBuffer -> Integer.toUnsignedString(byteBuffer.get() & 0xFF)),
    MessageType(53, "DHCP Message Type", b -> {
        int typeValue = b.get();
        Optional<DhcpMessageTypes> type = DhcpMessageTypes.get(typeValue);
        String name = type.map(DhcpMessageTypes::name).orElse("Unknown");
        return String.format("%d (%s)", typeValue, name);
    }),
    ServerId(54, "Server Identifier", DhcpOptionFormatter.IP),
    ParameterRequestList(55, "Parameter Request List", byteBuffer -> {
        StringJoiner s = new StringJoiner("\n        -> ", "\n        -> ", "");
        int size = byteBuffer.remaining();

        for (int i = 0; i < size; i++) {
            byte type = byteBuffer.get();
            s.add(String.format("Parameter List Item: %d (%s)", type & 0xFF, prettyNameFor(type)));
        }

        return s.toString();
    }),
    Message(56, "Message", DhcpOptionFormatter.ASCII),
    MaxMessageSize(57, "Maximum DHCP Message Size", DhcpOptionFormatter.UNSIGNED_SHORT),
    T1Value(58, "Renewal (T1) Time Value", DhcpOptionFormatter.UNSIGNED_INTEGER),
    T2Value(59, "Rebinding (T2) Time Value", DhcpOptionFormatter.UNSIGNED_INTEGER),
    VendorClassId(60, "Vendor class Identifier", DhcpOptionFormatter.RAW_HEX),
    ClientId(61, "Client Identifier", bytes -> {
        byte type = bytes.get();
        if (bytes.remaining() == 6 && type == 1) {
            try {
                // Will never throw
                return MacAddress.create(bytes).toString();
            } catch (NotEnoughBytesException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            String data = DhcpOptionFormatter.RAW_HEX.apply(bytes);
            return String.format("Type: 0x%02x, Data: %s", type, data);
        }
    }),
    TFTPName(66, "TFTP server name", DhcpOptionFormatter.ASCII),
    BootFileName(67, "Boot file name", DhcpOptionFormatter.ASCII),
    EndOfOptions(255, "End Of Options", DhcpOptionFormatter.EMPTY);

    // We put here options which have a fixed length
    // (it's assumed to be one because there isn't any other length in the RFC).
    private final static Set<Integer> FIXED_LENGTH = Set.of(DhcpOptions.Pad.value, DhcpOptions.EndOfOptions.value);

    public final static Map<Integer, DhcpOptions> VALUES = Arrays.stream(DhcpOptions.values())
            .collect(Collectors.toUnmodifiableMap(t -> t.value, t -> t));

    public int value;
    public String prettyName;
    public DhcpOptionFormatter formatter;

    DhcpOptions(int op, String pName, DhcpOptionFormatter f) {
        value = op;
        prettyName = pName;
        formatter = f;
    }

    private static String prettyNameFor(int code) {
        return get(code).map(v -> v.prettyName)
                .orElse("Unknown");
    }

    public static boolean isFixed(int t) {
        return FIXED_LENGTH.contains(t);
    }

    public static Optional<DhcpOptions> get(int code) {
        return Optional.ofNullable(VALUES.get(code));
    }
}
