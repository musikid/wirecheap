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
    Fallback(-1, "Unknown", DhcpOptionFormatter.RAW_HEX),
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
    PolicyFilter(21, "Policy Filter", DhcpOptionFormatter.IP),
    MaxDatagramRSize(22, "Max Datagram Reassembly Size", DhcpOptionFormatter.UNSIGNED_SHORT),
    DefaultTTL(23, "Default IP Time-to-live", DhcpOptionFormatter.UNSIGNED_BYTE),
    PathMTUTimeout(24, "Path MTU Aging Timeout", DhcpOptionFormatter.UNSIGNED_INTEGER),
    PathMTUTable(25, "Path MTU Plateau Table", buffer -> {
        // TODO: Change delimiter
        StringJoiner s = new StringJoiner(",");
        int nbCount = buffer.remaining() / 2;
        for (int i = 0; i < nbCount; i++) {
            s.add(DhcpOptionFormatter.UNSIGNED_SHORT.apply(buffer));
        }
        return s.toString();
    }),
    InterfaceMTU(26, "Interface MTU", DhcpOptionFormatter.UNSIGNED_SHORT),
    LocalSubnets(27, "All Subnets are Local", DhcpOptionFormatter.BOOLEAN),
    Broadcast(28, "Broadcast Address", DhcpOptionFormatter.IP),
    PerformDiscovery(29, "Perform Mask Discovery", DhcpOptionFormatter.BOOLEAN),
    MaskSupplier(30, "Mask Supplier", DhcpOptionFormatter.BOOLEAN),
    RouterDiscovery(31, "Perform Router Discovery", DhcpOptionFormatter.BOOLEAN),
    RouterSolAddress(32, "Router Solicitation Address", DhcpOptionFormatter.IP),
    StaticRoute(33, "Static Route", buffer -> {
        StringJoiner s = new StringJoiner(";");
        int count = buffer.remaining() / 8;
        for (int i = 0; i < count; i++) {
            String ip1 = DhcpOptionFormatter.IP.apply(buffer);
            String ip2 = DhcpOptionFormatter.IP.apply(buffer);
            s.add(String.format("(%s, %s)", ip1, ip2));
        }
        return s.toString();
    }),
    TrailerEnc(34, "Trailer Encapsulation", DhcpOptionFormatter.BOOLEAN),
    ArpCacheTO(35, "ARP Cache Timeout", DhcpOptionFormatter.UNSIGNED_INTEGER),
    EthernetEnc(36, "Ethernet Encapsulation", DhcpOptionFormatter.BOOLEAN),
    TCPDefaultTTL(37, "TCP Default TTL", DhcpOptionFormatter.UNSIGNED_BYTE),
    TCPKeepaliveInt(38, "TCP Keepalive Interval", DhcpOptionFormatter.UNSIGNED_INTEGER),
    TCPKeepaliveGar(39, "TCP Keepalive Garbage", DhcpOptionFormatter.BOOLEAN),
    NetInfoServiceDomain(40, "Network Information Service Domain", DhcpOptionFormatter.ASCII),
    NetInfoServers(41, "Network Information Servers", DhcpOptionFormatter.IP_LIST),
    NTPServers(42, "Network Time Protocol Servers", DhcpOptionFormatter.IP_LIST),
    Vendor(43, "Vendor Specific Information", DhcpOptionFormatter.RAW_HEX),
    NetBiosNameServer(44, "NetBIOS over TCP/IP Name Server", DhcpOptionFormatter.IP_LIST),
    NetBiosDataDisServer(45, "NetBIOS over TCP/IP Datagram Distribution Server", DhcpOptionFormatter.IP_LIST),
    NetBiosType(46, "", buffer -> {
        //TODO: Should we create a dedicated enum?
        return switch (buffer.get()) {
            case 0x1 -> "B-node";
            case 0x2 -> "P-node";
            case 0x4 -> "M-node";
            case 0x8 -> "H-node";
            default -> "Unknown node";
        };
    }),
    NetBiosScope(47, "NetBIOS over TCP/IP Scope", DhcpOptionFormatter.ASCII),
    XFontServer(48, "X Window System Font Server", DhcpOptionFormatter.IP_LIST),
    XDisplayServer(49, "X Window System Display Manager", DhcpOptionFormatter.IP_LIST),
    RequestedIp(50, "Requested IP Address", DhcpOptionFormatter.IP),
    LeaseTime(51, "IP Lease Time", DhcpOptionFormatter.UNSIGNED_INTEGER),
    OptionOverload(52, "Option Overload",
            byteBuffer -> {
                StringJoiner s = new StringJoiner(", ").setEmptyValue("Unknown overloading");
                byte value = byteBuffer.get();
                if ((value & DhcpOptionOverload.OverloadSname.value) > 0) {
                    s.add("sname is overloaded");
                }
                if ((value & DhcpOptionOverload.OverloadFile.value) > 0) {
                    s.add("file is overloaded");
                }
                return s.toString();
            }),
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
        // We try to display MAC address
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
    NetPlusDomain(64, "Network Information Service+ Domain", DhcpOptionFormatter.ASCII),
    NetPlusServer(65, "Network Information Service+ Servers", DhcpOptionFormatter.IP_LIST),
    TFTPName(66, "TFTP server name", DhcpOptionFormatter.ASCII),
    BootFileName(67, "Boot file name", DhcpOptionFormatter.ASCII),
    MobileIPAgent(68, "Mobile IP Home Agent", DhcpOptionFormatter.IP_LIST),
    SMTPServer(69, "Simple Mail Transport Protocol (SMTP) Server", DhcpOptionFormatter.IP_LIST),
    POP3Server(70, "Post Office Protocol (POP3) Server", DhcpOptionFormatter.IP_LIST),
    NNTPServer(71, "Network News Transport Protocol (NNTP) Server", DhcpOptionFormatter.IP_LIST),
    DefaultWWWServer(72, "Default World Wide Web (WWW) Server", DhcpOptionFormatter.IP_LIST),
    DefaultFingerServer(73, "Default Finger Server", DhcpOptionFormatter.IP_LIST),
    DefaultIRCServer(74, "Default Internet Relay Chat (IRC) Server", DhcpOptionFormatter.IP_LIST),
    StreetTalkServer(75, "StreetTalk Server", DhcpOptionFormatter.IP_LIST),
    STDAServer(76, "StreetTalk Directory Assistance (STDA) Server", DhcpOptionFormatter.IP_LIST),
    EndOfOptions(255, "End Of Options", DhcpOptionFormatter.EMPTY);

    public final static Map<Integer, DhcpOptions> VALUES = Arrays.stream(DhcpOptions.values())
            .collect(Collectors.toUnmodifiableMap(t -> t.value, t -> t));
    // We put here options which have a fixed length
    // (it's assumed to be one because there isn't any other length in the RFC).
    private final static Set<Integer> FIXED_LENGTH = Set.of(DhcpOptions.Pad.value, DhcpOptions.EndOfOptions.value);
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
