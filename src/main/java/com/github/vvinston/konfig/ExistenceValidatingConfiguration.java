package com.github.vvinston.konfig;

import java.util.Map;
import java.util.Optional;

public final class ExistenceValidatingConfiguration implements Configuration {
    private final Configuration configuration;

    public ExistenceValidatingConfiguration(final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Optional<ConfigValue> read(final String id) {
        final Optional<ConfigValue> result = configuration.read(id);
        if (!result.isPresent()) {
            throw new IllegalArgumentException("Configuration record " + id + "does not exist");
        }
        return result;
    }

    @Override
    public Map<String, ConfigValue> find(final String prefix) {
        return configuration.find(prefix);
    }
}
