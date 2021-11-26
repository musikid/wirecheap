package com.projet.parser.combinators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.projet.parser.combinators.Combinators.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CombinatorsTest {
    @Test
    @DisplayName("satisfy combinator")
    void testSatisfy() {
        final char testCase = 'a';
        final State<?> state = new State<>(testCase + "");
        assertTrue(satisfy(c -> c == testCase).apply(state));
        assertEquals(state.getResult(), testCase);
    }

    @Test
    @DisplayName("hexDigit combinator")
    void testHexDigit() {
        IntStream digitRange = IntStream.range(0, 0x10);
        for (char digit : digitRange.mapToObj(c -> Character.forDigit(c, 16)).collect(Collectors.toList())) {
            final State<?> state = new State<>(digit + "");
            assertTrue(hexDigit().apply(state));
            assertEquals(state.getResult(), digit);
        }
    }

    @Test
    @DisplayName("hexByte combinator")
    void testHexByte() {
        IntStream byteRange = IntStream.range(0, 0x100);
        for (String byteString : byteRange.mapToObj(c -> String.format("%02x", c)).collect(Collectors.toList())) {
            final State<?> state = new State<>(byteString);
            assertTrue(hexByte().apply(state));
            assertEquals(state.getResult(), (byte) Integer.parseInt(byteString, 16));
        }
    }
}
