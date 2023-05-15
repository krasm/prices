package net.krasm.pricecache;

import net.krasm.pricecache.config.PriceConfiguration;
import net.krasm.pricecache.repository.PriceRepositoryImpl;
import net.krasm.pricecache.service.PriceService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PriceServiceTest {

    private PriceService priceService;

    @BeforeEach
    public void setUp() {
        priceService = new PriceService(new PriceRepositoryImpl(), new PriceConfiguration(0.001, 0.001));
    }

    @Test
    void testSinglePriceChange() {
        priceService.onMessage("107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002");
        var p = priceService.findPriceByInstrumentName("EUR/JPY");
        assertTrue(p.isPresent());
        assertThat(p.get().name, Matchers.comparesEqualTo("EUR/JPY"));
        assertThat(BigDecimal.valueOf(119.48040), Matchers.comparesEqualTo(p.get().bid));
        assertThat(BigDecimal.valueOf(120.01990), Matchers.comparesEqualTo(p.get().ask));

        assertEquals(LocalDateTime.of(2020, 6, 1, 12, 1, 2, 2000000), p.get().timestamp);
    }

    @Test
    void testMultilineMessage() {
        final String message = "106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001\n" +
                "107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002\n" +
                "108, GBP/USD, 1.2500,1.2560,01-06-2020 12:01:02:002\n" +
                "109, GBP/USD, 1.2499,1.2561,01-06-2020 12:01:02:100\n" +
                "110, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110\n";

        priceService.onMessage(message);

        var p = priceService.findPriceByInstrumentName("EUR/JPY");
        assertTrue(p.isPresent());
        assertThat(p.get().name, Matchers.comparesEqualTo("EUR/JPY"));
        assertThat(BigDecimal.valueOf(120.02991), Matchers.comparesEqualTo(p.get().ask));
        assertThat(BigDecimal.valueOf(119.49039), Matchers.comparesEqualTo(p.get().bid));
        assertEquals(LocalDateTime.of(2020, 6, 1, 12, 1, 2, 110_000_000), p.get().timestamp);

        p = priceService.findPriceByInstrumentName("GBP/USD");
        assertTrue(p.isPresent());
        assertThat(p.get().name, Matchers.comparesEqualTo("GBP/USD"));
        assertThat(BigDecimal.valueOf(1.2486501), Matchers.comparesEqualTo(p.get().bid));
        assertThat(BigDecimal.valueOf(1.2573561), Matchers.comparesEqualTo(p.get().ask));
        assertEquals(LocalDateTime.of(2020, 6, 1, 12, 1, 2, 100_000_000), p.get().timestamp);

        p = priceService.findPriceByInstrumentName("EUR/USD");
        assertTrue(p.isPresent());
        assertThat(p.get().name, Matchers.comparesEqualTo("EUR/USD"));
        assertThat(BigDecimal.valueOf(1.0989000), Matchers.comparesEqualTo(p.get().bid));
        assertThat(BigDecimal.valueOf(1.2012000), Matchers.comparesEqualTo(p.get().ask));
        assertEquals(LocalDateTime.of(2020, 6, 1, 12, 1, 1, 1_000_000), p.get().timestamp);
    }
}