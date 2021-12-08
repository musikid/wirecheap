package com.lu3in033.projet.layers.ipv4;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TypeOfService {
    public byte rawValue;
    public String dscp;
    public String ecn;

    public TypeOfService(byte t) {
        rawValue = t;
        dscp = DSCP.get((rawValue & 0xFC) >>> 2).map(DSCP::name).orElse("Unknown");
        ecn = ECN.get(rawValue & 0x3).map(e -> e.prettyName).orElse("Unknown");
    }

    @Override
    public String toString() {
        return String.format("0x%02x (DSCP: %s, ECN: %s)", rawValue, dscp, ecn);
    }

    enum DSCP {
        CS6(48),
        EF(46),
        CS5(40),
        AF41(34),
        AF42(36),
        AF43(38),
        CS4(32),
        AF31(26),
        AF32(28),
        AF33(30),
        CS3(24),
        AF21(18),
        AF22(20),
        AF23(22),
        CS2(16),
        AF11(10),
        AF12(12),
        AF13(14),
        CS0(0),
        CS1(8);

        private final static Map<Integer, DSCP> VALUES = Arrays.stream(DSCP.values())
                .collect(Collectors.toUnmodifiableMap(v -> v.value, v -> v));
        public int value;

        DSCP(int v) {
            value = v;
        }

        public static Optional<DSCP> get(int v) {
            return Optional.ofNullable(VALUES.get(v));
        }
    }

    enum ECN {
        NotECT(0, "Non ECN-Capable Transport, Non-ECT"),
        ECT0(2, "ECN Capable Transport, ECT(0)"),
        ECT1(1, "ECN Capable Transport, ECT(1)"),
        CE(3, "Congestion Encountered, CE");

        private final static Map<Integer, ECN> VALUES = Arrays.stream(ECN.values())
                .collect(Collectors.toUnmodifiableMap(v -> v.value, v -> v));
        public int value;
        public String prettyName;

        ECN(int v, String n) {
            value = v;
            prettyName = n;
        }

        public static Optional<ECN> get(int v) {
            return Optional.ofNullable(VALUES.get(v));
        }
    }
}
