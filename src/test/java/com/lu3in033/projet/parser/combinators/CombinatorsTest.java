package com.lu3in033.projet.parser.combinators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.lu3in033.projet.parser.combinators.Combinators.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CombinatorsTest {
    @Test
    @DisplayName("satisfy combinator")
    void testSatisfy() throws ParseException {
        final char testCase = 'a';
        final State<?> state = new State<>(testCase + "");

        assertEquals(testCase, satisfy(c -> c == 'a').parse(state));
    }

    @Test
    @DisplayName("hexDigit combinator")
    void testHexDigit() throws ParseException {
        IntStream digitRange = IntStream.rangeClosed(0, 0xF);
        for (char digit : digitRange.mapToObj(i -> Character.forDigit(i, 16)).collect(Collectors.toList())) {
            assertEquals(digit, hexDigit().parse(digit + ""));

            // Test uppercase
            if (Character.isAlphabetic(digit)) {
                digit = Character.toUpperCase(digit);
                assertEquals(digit, hexDigit().parse(digit + ""));
            }
        }
    }

    @Test
    @DisplayName("hexByte combinator")
    void testHexByte() throws ParseException {
        IntStream byteRange = IntStream.rangeClosed(0, 0xFF);
        for (String byteString : byteRange.mapToObj(i -> String.format("%02x", i)).collect(Collectors.toList())) {
            assertEquals((byte) Integer.parseInt(byteString, 16), hexByte().parse(byteString));
        }
    }

    @Test
    @DisplayName("hexOffset combinator")
    void testHexOffset() throws ParseException {
        IntStream intRange = IntStream.range(0, 0xFFFF);
        for (Iterator<String> it = intRange.mapToObj(i -> String.format("%02x", i)).iterator(); it.hasNext(); ) {
            String offsetString = it.next();
            assertEquals(Integer.parseInt(offsetString, 16), hexOffset().parse(offsetString));
        }
    }

    @Test
    @DisplayName("space combinator")
    void testSpace() throws ParseException {
        char space = ' ';
        assertEquals(space, space().parse(space + ""));
    }

    @Test
    @DisplayName("spaces combinator")
    void testSpaces() throws ParseException {
        String testStr = "         ";
        State<String> state = new State<>(testStr);
        assertEquals(testStr.chars().mapToObj(c -> (char) c).collect(Collectors.toList()), spaces().parse(state));
        assertEquals(testStr.length(), state.checkpoint().pos);
    }

    @Test
    @DisplayName("count combinator")
    void testCount() throws ParseException {
        String testStr = "aaa";
        State<String> state = new State<>(testStr);
        assertEquals(testStr.chars().mapToObj(c -> (char) c).collect(Collectors.toList()),
                count(0, satisfy(c -> c == 'a')).parse(state));
        assertEquals(testStr.length(), state.checkpoint().pos);

        state = new State<>(testStr);
        assertEquals(testStr.chars().mapToObj(c -> (char) c).collect(Collectors.toList()),
                count(1, testStr.length(), satisfy(c -> c == 'a')).parse(state));
        assertEquals(testStr.length(), state.checkpoint().pos);

        state = new State<>("sts");
        assertEquals(0, count(0, testStr.length(), satisfy(c -> c == 'a')).parse(state).size());
        assertThrows(ParseException.class, () -> count(2, satisfy(c -> c == 's')).parse(""));
    }
}
