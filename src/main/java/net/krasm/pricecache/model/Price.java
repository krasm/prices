package net.krasm.pricecache.model;

import lombok.Builder;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@ToString
public class Price {
    // TODO clarify, for me it is instrument id not the line number in the input file !!!!
    public final int id;
    public final String name;
    public final BigDecimal bid;
    public final BigDecimal ask;
    public final LocalDateTime timestamp;

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Price) {
            Price price = (Price) obj;
            return Objects.equals(name, price.name);
        }

        return false;
    }
}
