package com.github.vvinston.konfig;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class CompositeConfiguration implements Configuration {

    private final Iterable<Configuration> configurations;

    public CompositeConfiguration(final Iterable<Configuration> configurations) {
        this.configurations = configurations;
    }

    @Override
    public Optional<ConfigValue> read(final String id) {
        for (final Configuration configuration : configurations) {
            final Optional<ConfigValue> candidate = configuration.read(id);
            if (candidate.isPresent()) {
                return candidate;
            }
        }

        return Optional.empty();
    }

    @Override
    public Map<String, ConfigValue> find(final String prefix) {
        final Map<String, ConfigValue> result = new HashMap<>();

        for (final Configuration configuration : configurations) {
            result.putAll(configuration.find(prefix));
        }

        return result;
    }
}
