package net.krasm.pricecache.controller;

import lombok.extern.slf4j.Slf4j;
import net.krasm.pricecache.service.PriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/price")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    /**
     * API method to retrieve latest available price for given instrument.
     *
     * @param instrumentName name of the instrument, e.g. "EUR/USD", "GBP/USD", "USD/JPY"
     * @return 404 if price not found, 200 with price when found
     * @implNote An exception may be thrown instead of returning 404, but it involves adding exception handlers, etc;
     * depends on the requirements exception with exception handler may be better option.
     */
    @GetMapping(path = "/latest", produces = "application/json")
    public ResponseEntity<?> getPriceByInstrumentName(@RequestParam("instrumentName") String instrumentName) {
        log.debug("Retrieving price for [{}]", instrumentName);
        var op = priceService.findPriceByInstrumentName(instrumentName);
        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Not part of API but for testing we need to provide method to load data
     *
     * @param messages in CSV format
     */
    @PostMapping(path = "/load", consumes = "text/plain")
    public void loadPrices(@RequestBody String messages) {
        log.info("Loading prices: {}", messages);
        priceService.onMessage(messages);
    }
}
