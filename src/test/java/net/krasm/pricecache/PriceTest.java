package net.krasm.pricecache;

import net.krasm.pricecache.model.Price;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PriceTest {

    @Test
    void testHashCode() {
        var p1 = Price.builder()
                .id(1)
                .ask(BigDecimal.valueOf(3.0))
                .bid(BigDecimal.valueOf(2.0))
                .name("TEST/1")
                .timestamp(LocalDateTime.now())
                .build();
        var p2 = Price.builder()
                .id(2)
                .ask(BigDecimal.valueOf(4.0))
                .bid(BigDecimal.valueOf(3.0))
                .name("TEST/2")
                .timestamp(LocalDateTime.now())
                .build();

        assertNotEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1, p2);
    }

    @Test
    void testEquals() {
        var p1 = Price.builder()
                .id(1)
                .ask(BigDecimal.valueOf(3.0))
                .bid(BigDecimal.valueOf(2.0))
                .name("TEST/1")
                .timestamp(LocalDateTime.now())
                .build();
        var p2 = Price.builder()
                .id(2)
                .ask(BigDecimal.valueOf(3.0))
                .bid(BigDecimal.valueOf(2.0))
                .name("TEST/1")
                .timestamp(LocalDateTime.now())
                .build();

        assertEquals(p1.hashCode(), p2.hashCode());
        assertEquals(p1, p2);

        p2 = Price.builder()
                .id(3)
                .ask(BigDecimal.valueOf(3.0))
                .bid(BigDecimal.valueOf(2.0))
                .name("TEST/2")
                .timestamp(LocalDateTime.now())
                .build();

        assertNotEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1, p2);
    }

    @Test
    void differentObject() {
        var p1 = Price.builder()
                .id(1)
                .ask(BigDecimal.valueOf(3.0))
                .bid(BigDecimal.valueOf(2.0))
                .name("TEST/1")
                .timestamp(LocalDateTime.now())
                .build();
        var p2 = new Object();

        assertNotEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1, p2);
    }
}