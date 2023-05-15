package net.krasm.pricecache;

import net.krasm.pricecache.controller.PriceController;
import net.krasm.pricecache.model.Price;
import net.krasm.pricecache.repository.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PricecacheApplicationTests {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private PriceController priceController;

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertThat(priceController).isNotNull();
    }

    @BeforeEach
    public void setup() {
        priceRepository.save(Price.builder()
                .id(19)
                .timestamp(LocalDateTime.of(2023, 5, 15, 19, 26, 33, 100200300))
                .name("TEST/BOOT")
                .bid(java.math.BigDecimal.ONE)
                .ask(java.math.BigDecimal.TEN)
                .build());

        priceRepository.save(Price.builder()
                .id(1)
                .timestamp(LocalDateTime.of(2023, 5, 15, 19, 26, 33, 100200300))
                .name("BOOT/TEST")
                .bid(java.math.BigDecimal.TEN)
                .ask(java.math.BigDecimal.ONE)
                .build());
    }

    @Test
    void weCanRetrievePrice() throws Exception {
        var actual = this.restTemplate
                .getForEntity("http://localhost:" + port + "/price/latest?instrumentName=TEST/BOOT", Price.class);
        assertThat(actual.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(actual.getBody()).isNotNull();
        assertThat(actual.getBody().id).isEqualTo(19);
        assertThat(actual.getBody().name).isEqualTo("TEST/BOOT");
        assertThat(actual.getBody().bid).isEqualTo(java.math.BigDecimal.ONE);
        assertThat(actual.getBody().ask).isEqualTo(java.math.BigDecimal.TEN);
        assertThat(actual.getBody().timestamp).isEqualTo(LocalDateTime.of(2023, 5, 15, 19, 26, 33, 100200300));
    }

    @Test
    void whenNoPriceFound404isReturn() throws Exception {
        var actual = this.restTemplate
                .getForEntity("http://localhost:" + port + "/price/latest?instrumentName=TEST/BOOT2", Price.class);
        assertThat(actual.getStatusCode().is4xxClientError()).isTrue();
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
