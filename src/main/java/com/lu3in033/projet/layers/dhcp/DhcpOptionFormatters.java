package com.lu3in033.projet.layers.dhcp;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Map.entry;

//TODO: Embed directly in the enum?
public class DhcpOptionFormatters {
    // see RFC2131 table 3 (page 28)
    private final static Map<Integer, Function<List<Byte>, String>> map = Map.ofEntries(
            entry(DhcpOptions.LeaseTime.value,
                    (bytes) -> Integer.toUnsignedString(((bytes.get(0) & 0xFF) << 24) |
                            ((bytes.get(1) & 0xFF) << 16) |
                            ((bytes.get(2) & 0xFF) << 8) |
                            ((bytes.get(3) & 0xFF)))
            ),
            // TODO: Complete option overload
            // entry(DhcpOptions.OptionOverload.value, (bytes -> ))
            entry(DhcpOptions.MessageType.value, (b) -> "")
    );
}
