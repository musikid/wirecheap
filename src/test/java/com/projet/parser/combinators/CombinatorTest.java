package com.projet.parser.combinators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projet.parser.combinators.Combinators.satisfy;
import static com.projet.parser.combinators.Combinators.space;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CombinatorTest {
    @Test
    @DisplayName("skip")
    void testSkip() {
        State<?> testCase = new State<>("a s");
        State<?> testCase2 = new State<>("a s");

        Combinator<?> testCombinator = satisfy(c -> c == 'a');
        Combinator<?> testSkipCombinator = testCombinator.skip(space());

        assertTrue(testSkipCombinator.apply(testCase));
        assertEquals('a', testCase.getResult());
        assertEquals(2, testCase.checkpoint().pos);

        // We want to ensure that the skip parser works correctly
        assertTrue(testCombinator.apply(testCase2));
        assertEquals('a', testCase2.getResult());
        assertEquals(1, testCase2.checkpoint().pos);
    }
}
