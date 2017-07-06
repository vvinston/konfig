package com.github.vvinston.konfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class StaticConfiguration implements Configuration {

    private final Configuration configuration;

    public StaticConfiguration(final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Optional<ConfigValue> read(final String id) {
        final Optional<ConfigValue> result = configuration.read(id);
        return result.isPresent()
                ? Optional.of(new StaticConfigValue(result.get().asString()))
                : Optional.empty();
    }

    @Override
    public Map<String, ConfigValue> find(final String prefix) {
        final Map<String, ConfigValue> result = new HashMap<>();

        for(final Map.Entry<String, ConfigValue> record : configuration.find(prefix).entrySet()) {
            result.put(record.getKey(), new StaticConfigValue(record.getValue().asString()));
        }

        return result;
    }
}
