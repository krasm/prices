package net.krasm.pricecache.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CVSParserTest {

    @Test
    void parseNullOrEmpty() {
        var p = CVSParser.parse(null);
        assertTrue(p.isEmpty());
        p = CVSParser.parse("");
        assertTrue(p.isEmpty());
    }

    @Test
    void parseIncomplete() {
        var p = CVSParser.parse("1,2,3");
        assertTrue(p.isEmpty());
    }

    @Test
    void parseInvalidNumber() {
        var p = CVSParser.parse("1,EUR/PLN,a,4.0,01-06-2020 12:01:01:002");
        assertTrue(p.isEmpty());
    }

    @Test
    void parseInvalidTimestamp() {
        var p = CVSParser.parse("1,EUR/PLN,3.0,4.0,01:06:2020 12:01:01:002");
        assertTrue(p.isEmpty());
    }
}