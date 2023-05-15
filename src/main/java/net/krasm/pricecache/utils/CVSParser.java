package net.krasm.pricecache.utils;

import lombok.extern.slf4j.Slf4j;
import net.krasm.pricecache.model.Price;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class CVSParser {
    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS");

    /**
     * Poor man CSV parser, does not handle quoted values.
     *
     * @param line
     * @return parsed data
     */
    private static String[] extractRaw(String line) {

        if (line == null || line.isEmpty()) {
            return new String[]{}; // nulls are evil
        }
        return Arrays.stream(line.split(","))
                .map(String::trim).toArray(String[]::new);
    }

    public static Optional<Price> parse(String line) {
        var values = extractRaw(line);

        if (values.length == 0) {
            return Optional.empty();
        }

        if (values.length != 5) {
            log.warn("Invalid message: [{}]", line);
            // we could throw an exception here, but I prefer to ignore invalid messages
            return Optional.empty();
        }

        try {
            final LocalDateTime timestamp = LocalDateTime.parse(values[4], dateTimeFormatter);
            var p = Price.builder()
                    .id(Integer.parseInt(values[0]))
                    .name(values[1])
                    .bid(new BigDecimal(values[2]))
                    .ask(new BigDecimal(values[3]))
                    .timestamp(timestamp)
                    .build();
            return Optional.of(p);
        } catch (NumberFormatException | DateTimeParseException e) {
            log.warn("Failed to parse line: {}", line, e);
            return Optional.empty();
        }
    }
}
