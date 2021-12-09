package com.lu3in033.projet.parser;

import com.lu3in033.projet.parser.combinators.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lu3in033.projet.parser.Parser.fragment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {
    @Test
    @DisplayName("fragment combinator")
    void testFragment() throws ParseException {
        String fragmentString = "0000  54 8d 5a 56 c7 55 be 9a e5 d5 31 7d 08 00 45 28  T.ZV.U....1}..E(";
        List<Byte> expectedList = Stream
                .of(0x54, 0x8d, 0x5a, 0x56, 0xc7, 0x55, 0xbe, 0x9a, 0xe5, 0xd5, 0x31, 0x7d, 0x08, 0x00, 0x45, 0x28)
                .map(Integer::byteValue).collect(Collectors.toList());
        Fragment fragment = new Fragment(0, expectedList);
        assertEquals(fragment, fragment().parse(fragmentString));
    }

    @Test
    @DisplayName("parse function")
    void testParser() throws ParseException {
        ByteBuffer b1 = ByteBuffer.allocate(0x36);
        b1.put((byte) 0x54).put((byte) 0x8d).put((byte) 0x5a).put((byte) 0x56).put((byte) 0xc7)
                .put((byte) 0x55).put((byte) 0xbe).put((byte) 0x9a).put((byte) 0xe5)
                .put((byte) 0xd5).put((byte) 0x31).put((byte) 0x7d).put((byte) 0x08)
                .put((byte) 0x00).put((byte) 0x45).put((byte) 0x28).put((byte) 0x00)
                .put((byte) 0x28).put((byte) 0xc3).put((byte) 0xec).put((byte) 0x40)
                .put((byte) 0x00).put((byte) 0xfb).put((byte) 0x06).put((byte) 0xf6)
                .put((byte) 0xfd).put((byte) 0x33).put((byte) 0x90).put((byte) 0xa4)
                .put((byte) 0xd7).put((byte) 0xc0).put((byte) 0xa8).put((byte) 0x2b)
                .put((byte) 0xad).put((byte) 0x01).put((byte) 0xbb).put((byte) 0xe8)
                .put((byte) 0xe0).put((byte) 0x28).put((byte) 0x67).put((byte) 0x1c)
                .put((byte) 0xfe).put((byte) 0x72).put((byte) 0x7c).put((byte) 0x20)
                .put((byte) 0x72).put((byte) 0x50).put((byte) 0x14).put((byte) 0x00)
                .put((byte) 0x00).put((byte) 0x28).put((byte) 0x24).put((byte) 0x00)
                .put((byte) 0x00).rewind();
        ByteBuffer b2 = ByteBuffer.allocate(0x36);
        b2.put((byte) 0xbe).put((byte) 0x9a).put((byte) 0xe5).put((byte) 0xd5).put((byte) 0x31)
                .put((byte) 0x7d).put((byte) 0x54).put((byte) 0x8d).put((byte) 0x5a)
                .put((byte) 0x56).put((byte) 0xc7).put((byte) 0x55).put((byte) 0x08)
                .put((byte) 0x00).put((byte) 0x45).put((byte) 0x00).put((byte) 0x00)
                .put((byte) 0x28).put((byte) 0x27).put((byte) 0x1b).put((byte) 0x00)
                .put((byte) 0x00).put((byte) 0x80).put((byte) 0x06).put((byte) 0x00)
                .put((byte) 0x00).put((byte) 0xc0).put((byte) 0xa8).put((byte) 0x2b)
                .put((byte) 0xad).put((byte) 0xa2).put((byte) 0x9f).put((byte) 0x86)
                .put((byte) 0xea).put((byte) 0xe6).put((byte) 0x63).put((byte) 0x01)
                .put((byte) 0xbb).put((byte) 0xed).put((byte) 0x29).put((byte) 0x53)
                .put((byte) 0xc0).put((byte) 0x32).put((byte) 0x0a).put((byte) 0xae)
                .put((byte) 0x3d).put((byte) 0x50).put((byte) 0x10).put((byte) 0x00)
                .put((byte) 0xff).put((byte) 0x8f).put((byte) 0xa5).put((byte) 0x00)
                .put((byte) 0x00).rewind();
        List<Frame> expected = Arrays.asList(new Frame(1, b1), new Frame(2, b2));

        String testCase = """
                0000 54 8d 5a 56 c7 55 be 9a e5 d5 31 7d 08 00 45 28
                0010 00 28 c3 ec 40 00 fb 06 f6 fd 33 90 a4 d7 c0 a8
                this is a comment line
                0020 2b ad 01 bb e8 e0 28 67 1c fe 72 7c 20 72 50 14
                0 fake offset
                0030 00 00 28 24 00 00
                a comment before another frame
                0000  be 9a e5 d5 31 7d 54 8d 5a 56 c7 55 08 00 45 00   ....1}T.ZV.U..E.
                0010  00 28 27 1b 00 00 80 06 00 00 c0 a8 2b ad a2 9f   .('.........+...
                0020  86 ea e6 63 01 bb ed 29 53 c0 32 0a ae 3d 50 10   ...c...)S.2..=P.
                0030  00 ff 8f a5 00 00                                 ......
                """;

        Parser parser = new Parser();
        List<Frame> actual = parser.parse(testCase);
        assertEquals(expected, actual);

        String failTestCaseHighOffset = """
                0000  54 8d 5a 56 c7 55 be 9a e5 d5 31 7d 08 00 45 28   T.ZV.U....1}..E(
                0010  00 28 c3 ec 40 00 fb 06 f6 fd 33 90 a4 d7 c0 a8   .(..@.....3.....
                0020  2b ad 01 bb e8 e0 28 67 1c fe 72 7c 20 72 50 14   +.....(g..r| rP.
                0030  00 00 28 24 00 00                                 ..($..

                0000  be 9a e5 d5 31 7d 54 8d 5a 56 c7 55 08 00 45 00   ....1}T.ZV.U..E.
                0010  00 28 27 1b 00 00 80 06 00 00 c0 a8 2b ad a2 9f   .('.........+...
                0030  00 ff 8f a5 00 00                                 ......
                """;

        Parser failParser = new Parser();
        assertThrows(ParseException.class, () -> failParser.parse(failTestCaseHighOffset));

        String failTestCaseHexByte = """
                0000  54 8d 5aaa 56 c7 55 be 9a e5 d5 31 7d 08 00 45 28   T.ZV.U....1}..E(
                0010  00 28 c3 ec 40 00 fb 06 f6 fd 33 90 a4 d7 c0 a8   .(..@.....3.....
                0020  2b ad 01 bb e8 e0 28 67 1c fe 72 7c 20 72 50 14   +.....(g..r| rP.
                0030  00 00 28 24 00 00                                 ..($..

                0000  be 9a e5 d5 31 7d 54 8d 5a 56 c7 55 08 00 45 00   ....1}T.ZV.U..E.
                0010  00 28 27 1b 00 00 80 06 00 00 c0 a8 2b ad a2 9f   .('.........+...
                0030  00 ff 8f a5 00 00                                 ......
                """;

        assertThrows(ParseException.class, () -> failParser.parse(failTestCaseHexByte));

        String failTestCaseLowOffset = """
                0000  54 8d 5aaa 56 c7 55 be 9a e5 d5 31 7d 08 00 45 28   T.ZV.U....1}..E(
                0010  00 28 c3 ec 40 00 fb 06 f6 fd 33 90 a4 d7 c0 a8   .(..@.....3.....
                0010  2b ad 01 bb e8 e0 28 67 1c fe 72 7c 20 72 50 14   +.....(g..r| rP.
                0030  00 00 28 24 00 00                                 ..($..

                0000  be 9a e5 d5 31 7d 54 8d 5a 56 c7 55 08 00 45 00   ....1}T.ZV.U..E.
                0010  00 28 27 1b 00 00 80 06 00 00 c0 a8 2b ad a2 9f   .('.........+...
                0030  00 ff 8f a5 00 00                                 ......
                """;

        assertThrows(ParseException.class, () -> failParser.parse(failTestCaseLowOffset));
    }
}
