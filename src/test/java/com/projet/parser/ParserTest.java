package com.projet.parser;

import com.projet.parser.combinators.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.projet.parser.Parser.fragment;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {
    @Test
    @DisplayName("fragment combinator")
    void testFragment() throws ParseException {
        String fragmentString = "0000  54 8d 5a 56 c7 55 be 9a e5 d5 31 7d 08 00 45 28  T.ZV.U....1}..E(";
        int[] expectedListInt = new int[]{
                0x54, 0x8d, 0x5a, 0x56, 0xc7, 0x55, 0xbe, 0x9a, 0xe5, 0xd5, 0x31, 0x7d, 0x08, 0x00, 0x45, 0x28,
        };
        List<Byte> expectedList = Arrays
                .stream(expectedListInt)
                .mapToObj(b -> (byte) b)
                .collect(Collectors.toList());
        Fragment fragment = new Fragment(0, expectedList);
        assertEquals(fragment, fragment().parse(fragmentString));
    }

    @Test
    @DisplayName("parse function")
    void testParser() throws ParseException {
        List<Frame> expected = Arrays.asList(
                new Frame(1, Stream.of(
                        0x54, 0x8d, 0x5a, 0x56, 0xc7, 0x55, 0xbe, 0x9a, 0xe5, 0xd5, 0x31, 0x7d, 0x08, 0x00, 0x45, 0x28,
                        0x00, 0x28, 0xc3, 0xec, 0x40, 0x00, 0xfb, 0x06, 0xf6, 0xfd, 0x33, 0x90,
                        0xa4, 0xd7, 0xc0, 0xa8,
                        0x2b, 0xad, 0x01, 0xbb, 0xe8, 0xe0, 0x28, 0x67, 0x1c, 0xfe, 0x72, 0x7c,
                        0x20, 0x72, 0x50, 0x14,
                        0x00, 0x00, 0x28, 0x24, 0x00, 0x00
                ).map(Integer::byteValue).collect(Collectors.toList())),
                new Frame(2,
                        Stream.of(
                                0xbe, 0x9a, 0xe5, 0xd5, 0x31, 0x7d, 0x54, 0x8d, 0x5a, 0x56, 0xc7, 0x55,
                                0x08, 0x00, 0x45, 0x00, 0x00, 0x28, 0x27, 0x1b, 0x00, 0x00, 0x80, 0x06, 0x00, 0x00, 0xc0, 0xa8,
                                0x2b, 0xad, 0xa2, 0x9f, 0x86, 0xea, 0xe6, 0x63, 0x01, 0xbb, 0xed, 0x29, 0x53, 0xc0, 0x32, 0x0a,
                                0xae, 0x3d, 0x50, 0x10, 0x00, 0xff, 0x8f, 0xa5, 0x00, 0x00
                        ).map(Integer::byteValue).collect(Collectors.toList()))
        );


        String testCase = """
                0000  54 8d 5a 56 c7 55 be 9a e5 d5 31 7d 08 00 45 28   T.ZV.U....1}..E(
                0010  00 28 c3 ec 40 00 fb 06 f6 fd 33 90 a4 d7 c0 a8   .(..@.....3.....
                0020  2b ad 01 bb e8 e0 28 67 1c fe 72 7c 20 72 50 14   +.....(g..r| rP.
                0030  00 00 28 24 00 00                                 ..($..

                0000  be 9a e5 d5 31 7d 54 8d 5a 56 c7 55 08 00 45 00   ....1}T.ZV.U..E.
                0010  00 28 27 1b 00 00 80 06 00 00 c0 a8 2b ad a2 9f   .('.........+...
                0020  86 ea e6 63 01 bb ed 29 53 c0 32 0a ae 3d 50 10   ...c...)S.2..=P.
                0030  00 ff 8f a5 00 00                                 ......
                """;

        Parser parser = new Parser();
        List<Frame> actual = parser.parse(testCase);
        assertEquals(expected, actual);
    }
}
