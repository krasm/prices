package net.krasm.pricecache.repository;

import net.krasm.pricecache.model.Price;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PriceRepositoryImplTest {
    private final PriceRepositoryImpl priceRepository = new PriceRepositoryImpl();

    @Test
    void save() {
        final String instrumentName = "TEST/1";
        final LocalDateTime now = LocalDateTime.now();
        priceRepository.save(Price.builder()
                .id(19)
                .timestamp(now)
                .name("TEST/1")
                .bid(BigDecimal.ONE)
                .ask(BigDecimal.TEN)
                .build());

        var p = priceRepository.findByInstrumentName(instrumentName);
        assertTrue(p.isPresent());
        assertEquals(19, p.get().id);
        assertEquals(now, p.get().timestamp);
        assertEquals(instrumentName, p.get().name);
        assertEquals(BigDecimal.ONE, p.get().bid);
        assertEquals(BigDecimal.TEN, p.get().ask);
    }

    @Test
    void findByInstrumentName() {
        final String instrumentName = "TEST/2";
        var p = priceRepository.findByInstrumentName(null);
        assertFalse(p.isPresent());
        p = priceRepository.findByInstrumentName(instrumentName);
        assertFalse(p.isPresent());
    }

    @Test
    void discardOlderUpdate() {
        final String instrumentName = "TEST/3";
        final LocalDateTime now = LocalDateTime.now();
        priceRepository.save(Price.builder()
                .id(19)
                .timestamp(now)
                .name(instrumentName)
                .bid(BigDecimal.ONE)
                .ask(BigDecimal.TEN)
                .build());

        priceRepository.save(Price.builder()
                .id(16)
                .timestamp(now.minus(1, java.time.temporal.ChronoUnit.SECONDS))
                .name(instrumentName)
                .bid(BigDecimal.ONE.add(BigDecimal.ONE))
                .ask(BigDecimal.TEN.add(BigDecimal.ONE))
                .build());

        var p = priceRepository.findByInstrumentName(instrumentName);
        assertTrue(p.isPresent());
        assertEquals(BigDecimal.ONE, p.get().bid);
        assertEquals(BigDecimal.TEN, p.get().ask);
        assertEquals(now, p.get().timestamp);
    }
}