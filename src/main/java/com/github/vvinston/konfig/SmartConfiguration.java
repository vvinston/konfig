package com.github.vvinston.konfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public final class SmartConfiguration  implements Configuration {

    private final Configuration configuration;
    private final Predicate<ConfigValue> predicate;

    public SmartConfiguration(final Configuration configuration, final Predicate<ConfigValue> predicate) {
        this.configuration = configuration;
        this.predicate = predicate;
    }

    @Override
    public Optional<ConfigValue> read(final String id) {
        final Optional<ConfigValue> result = configuration.read(id);
        return result.isPresent() && predicate.test(result.get())
                ? read(result.get().asString())
                : result;
    }

    @Override
    public Map<String, ConfigValue> find(final String prefix) {
        final Map<String, ConfigValue> result = new HashMap<>();
        for (final Map.Entry<String, ConfigValue> record : configuration.find(prefix).entrySet()) {
            final Optional<ConfigValue> candidate = predicate.test(record.getValue())
                    ? read(record.getKey())
                    : Optional.of(record.getValue());
            if (candidate.isPresent()) {
                result.put(record.getKey(), candidate.get());
            }
        }
        return result;
    }
}
