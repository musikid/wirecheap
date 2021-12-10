package com.lu3in033.projet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ErrorReporterTest {
    @Test
    @DisplayName("reporter class")
    void reporterTest() {
        String extract = "befare";
        var reporter = new ErrorReporter(extract, 0, 3, 1, "o");
        Assertions.assertEquals("""
                Error at line 1, character 3
                befare
                  ^
                Expected: o""", reporter.toString());
    }
}
