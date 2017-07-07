package com.github.vvinston.konfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class DifferenceDynamicConfiguration implements Configuration, DynamicConfiguration {

    private final Configuration configuration;
    private final Map<String, String> overwritten = new HashMap<>();

    public DifferenceDynamicConfiguration(final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void change(final String id, final String value) {
        overwritten.put(id, value);
    }

    @Override
    public Optional<ConfigValue> read(final String id) {
        return overwritten.containsKey(id)
                ? Optional.of(new StaticConfigValue(overwritten.get(id)))
                : configuration.read(id);
    }

    @Override
    public Map<String, ConfigValue> find(final String prefix) {
        final Map<String, ConfigValue> result = configuration.find(prefix);
        for (final String key : result.keySet()) {
            if (overwritten.containsKey(key)) {
                result.put(key, new StaticConfigValue(overwritten.get(key)));
            }
        }
        return result;
    }

    public void reset() {
        overwritten.clear();
    }
}
