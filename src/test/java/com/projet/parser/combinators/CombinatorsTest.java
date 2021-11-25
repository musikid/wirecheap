package com.projet.parser.combinators;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CombinatorsTest {
    @Test
    @DisplayName("satisfy combinator")
    void testSatisfy() throws ParseException {
        final String testCase = "a";
        assert(Combinators.satisfy(c -> c == 'a').apply(new State<>(testCase)));
    }
}
