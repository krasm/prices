package net.krasm.pricecache.repository;

import net.krasm.pricecache.model.Price;

import java.util.Optional;

public interface PriceRepository {
    void save(Price price);

    Optional<Price> findByInstrumentName(String name);
}
