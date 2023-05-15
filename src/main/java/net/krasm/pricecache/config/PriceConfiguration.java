package net.krasm.pricecache.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "pricecache")
public class PriceConfiguration {

    private double bidMargin;
    private double askMargin;

    public PriceConfiguration() {
        this(0.0, 0.0);
    }

    public PriceConfiguration(double bidMargin, double askMargin) {
        this.bidMargin = bidMargin;
        this.askMargin = askMargin;
    }
}
