package net.krasm.pricecache.service;

import lombok.extern.slf4j.Slf4j;
import net.krasm.pricecache.MessagingClient;
import net.krasm.pricecache.config.PriceConfiguration;
import net.krasm.pricecache.model.Price;
import net.krasm.pricecache.repository.PriceRepository;
import net.krasm.pricecache.utils.CVSParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j
public class PriceService implements MessagingClient {


    private final Function<String, Optional<Price>> messageProcessor;

    private final PriceRepository repository;

    public PriceService(PriceRepository repository, PriceConfiguration config) {
        this.repository = repository;

        // TODO: instead of simple lambda it would be better to extract it to a separate service
        //       which can be injected; service like price calculator
        this.messageProcessor = (String message) -> CVSParser.parse(message)
                .map(p -> {
                    var bid = p.bid.multiply(BigDecimal.valueOf(1.0 - config.getBidMargin()));
                    var ask = p.ask.multiply(BigDecimal.valueOf(1.0 + config.getAskMargin()));

                    log.info("Storing price: name: [{}], bid: [{}], ask: [{}], timestamp: [{}]",
                            p.name, bid, ask, p.timestamp);
                    return Price.builder()
                            .id(p.id)
                            .name(p.name)
                            .bid(bid)
                            .ask(ask)
                            .timestamp(p.timestamp)
                            .build();
                });
    }

    @Override
    public void onMessage(String message) {
        log.info("Received message: {}", message);
        Arrays.stream(message.split("\n")).parallel().forEach(c -> {
                    messageProcessor.apply(c).ifPresent(repository::save);
                }
        );
    }

    public Optional<Price> findPriceByInstrumentName(String name) {
        log.debug("Retrieving price for [{}]", name);
        return repository.findByInstrumentName(name);
    }
}
