package com.projet.parser.combinators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CombinatorsTest {
    @Test
    @DisplayName("satisfy combinator")
    void testSatisfy() {
        final String testCase = "a";
        assert (Combinators.satisfy(c -> c == 'a').apply(new State<>(testCase)));
    }

    @Test
    @DisplayName("hexDigit combinator")
    void testHexDigit() {
        IntStream letters = IntStream.concat(IntStream.range('a', 'f' + 1), IntStream.range('a', 'F' + 1));
        IntStream digits = IntStream.range('0', '9' + 1);
        for (char digit : digits.mapToObj(c -> (char) c).collect(Collectors.toList())) {
            assert (Combinators.hexDigit().apply(new State<>(digit + "")));
        }
    }
}
