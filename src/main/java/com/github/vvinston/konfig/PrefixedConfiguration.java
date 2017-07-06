package com.github.vvinston.konfig;

import java.util.Map;
import java.util.Optional;

public final class PrefixedConfiguration implements Configuration {

    private final Configuration configuration;
    private final String configurationPrefix;

    public PrefixedConfiguration(final Configuration configuration, final String configurationPrefix) {
        this.configuration = configuration;
        this.configurationPrefix = configurationPrefix;
    }

    @Override
    public Optional<ConfigValue> read(final String id) {
        return id.startsWith(configurationPrefix)
                ? configuration.read(id.substring(configurationPrefix.length()))
                : Optional.empty();
    }

    @Override
    public Map<String, ConfigValue> find(final String prefix) {
        return configuration.find(prefix.substring(configurationPrefix.length()));
    }
}
