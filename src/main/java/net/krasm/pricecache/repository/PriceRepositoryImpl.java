package net.krasm.pricecache.repository;

import net.krasm.pricecache.model.Price;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class PriceRepositoryImpl implements PriceRepository {
    private final Map<String, Price> prices = new HashMap<>();

    @Override
    public void save(Price price) {
        synchronized (prices) {
            Price p = prices.get(price.name);
            if (p == null || p.timestamp.isBefore(price.timestamp)) {
                prices.put(price.name, price);
            }
        }
    }

    @Override
    public Optional<Price> findByInstrumentName(String name) {
        synchronized (prices) {
            return Optional.ofNullable(prices.get(name));
        }
    }
}
