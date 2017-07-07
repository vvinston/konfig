package com.github.vvinston.konfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class DifferenceOverwritableConfiguration implements OverwritableConfiguration {

    private final Configuration configuration;
    private final Map<String, String> overwritten = new HashMap<>();

    public DifferenceOverwritableConfiguration(final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void overwrite(final String id, final String value) {
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
