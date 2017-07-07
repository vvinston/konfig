package com.github.vvinston.konfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class CachedConfiguration implements Configuration {

    private final Configuration configuration;
    private final Map<String, Optional<ConfigValue>> cache = new HashMap<>();

    public CachedConfiguration(final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Optional<ConfigValue> read(final String id) {
        if (!cache.containsKey(id)) {
            cache.put(id, configuration.read(id));
        }

        return cache.get(id);
    }

    @Override
    public Map<String, ConfigValue> find(final String prefix) {
        // This might be cached too
        return configuration.find(prefix);
    }

    void invalidate() {
        cache.clear();
    }
}
