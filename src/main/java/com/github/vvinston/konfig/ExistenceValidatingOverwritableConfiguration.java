package com.github.vvinston.konfig;

import java.util.Map;
import java.util.Optional;

public class ExistenceValidatingOverwritableConfiguration implements OverwritableConfiguration {

    private final OverwritableConfiguration configuration;

    public ExistenceValidatingOverwritableConfiguration(final OverwritableConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void change(final String id, final String value) {
        if (!configuration.read(id).isPresent()) {
            throw new IllegalArgumentException("Not an existing config: " + id);
        }
    }

    @Override
    public Optional<ConfigValue> read(final String id) {
        return configuration.read(id);
    }

    @Override
    public Map<String, ConfigValue> find(final String prefix) {
        return configuration.find(prefix);
    }
}
