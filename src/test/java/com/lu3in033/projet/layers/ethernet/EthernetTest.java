package com.lu3in033.projet.layers.ethernet;

import com.lu3in033.projet.layers.NotEnoughBytesException;
import com.lu3in033.projet.parser.Frame;
import com.lu3in033.projet.parser.Parser;
import com.lu3in033.projet.parser.combinators.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.List;

public class EthernetTest {
    @Test
    @DisplayName("Ethernet class creation")
    void ethernetTest() throws ParseException, NotEnoughBytesException {
        var testCase = """
                0000  ff ff ff ff ff ff fa fa fa fa fa fa 08 00 45 c0   ........g..k..E.
                """;
        MacAddress dest = new MacAddress(new byte[]{
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff
        });
        MacAddress src = new MacAddress(new byte[]{
                (byte) 0xfa, (byte) 0xfa, (byte) 0xfa, (byte) 0xfa, (byte) 0xfa, (byte) 0xfa
        });
        ByteBuffer payload = ByteBuffer.allocate(2).put((byte) 0x45).put((byte) 0xc0);
        Ethernet expected = new Ethernet(dest, src, new EtherType((short) 0x0800), payload);


        List<Frame> frames = new Parser().parse(testCase);
        Ethernet actual = Ethernet.create(frames.get(0).buffer);

        Assertions.assertEquals(expected, actual);
    }
}
