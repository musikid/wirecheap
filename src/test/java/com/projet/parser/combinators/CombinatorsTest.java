package com.projet.parser.combinators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
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
        IntStream digitRange = IntStream.rangeClosed(0, 0xF);
        for (char digit : digitRange.mapToObj(i -> Character.forDigit(i, 16)).collect(Collectors.toList())) {
            final State<?> state = new State<>(digit + "");

            assertTrue(hexDigit().apply(state));
            assertEquals(state.getResult(), digit);
        }
    }

    @Test
    @DisplayName("hexByte combinator")
    void testHexByte() {
        IntStream byteRange = IntStream.rangeClosed(0, 0xFF);
        for (String byteString : byteRange.mapToObj(i -> String.format("%02x", i)).collect(Collectors.toList())) {
            final State<?> state = new State<>(byteString);

            assertTrue(hexByte().apply(state));
            assertEquals(state.getResult(), (byte) Integer.parseInt(byteString, 16));
        }
    }

    @Test
    @DisplayName("hexOffset combinator")
    void testHexOffset() {
        IntStream intRange = IntStream.range(0, 0xFFFF);
        for (Iterator<String> it = intRange.mapToObj(i -> String.format("%02x", i)).iterator(); it.hasNext(); ) {
            String offsetString = it.next();
            final State<?> state = new State<>(offsetString);

            assertTrue(hexOffset().apply(state));
            assertEquals(state.getResult(), Integer.parseInt(offsetString, 16));
        }
    }
}
