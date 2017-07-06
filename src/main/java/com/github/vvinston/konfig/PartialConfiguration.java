package com.github.vvinston.konfig;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public final class PartialConfiguration implements Configuration {

    private final Configuration configuration;
    private final String configurationPrefix;

    public PartialConfiguration(final Configuration configuration, final String configurationPrefix) {
        this.configuration = configuration;
        this.configurationPrefix = configurationPrefix;
    }

    @Override
    public Optional<ConfigValue> read(final String id) {
        return id.startsWith(configurationPrefix) ? configuration.read(id) : Optional.empty();
    }

    @Override
    public Map<String, ConfigValue> find(final String prefix) {
        return prefix.startsWith(configurationPrefix) ? configuration.find(prefix) : Collections.emptyMap();
    }
}
