package com.github.vvinston.konfig;

import java.util.Map;
import java.util.Optional;

public class NotExpandingDynamicConfiguration<T extends Configuration & DynamicConfiguration> implements Configuration, DynamicConfiguration {

    private final T configuration;

    public NotExpandingDynamicConfiguration(final T configuration) {
        this.configuration = configuration;
    }

    @Override
    public void change(final String id, final String value) {
        if (!configuration.read(id).isPresent()) {
            throw new IllegalArgumentException("Not an existing config: " + id);
        }

        configuration.change(id, value);
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
