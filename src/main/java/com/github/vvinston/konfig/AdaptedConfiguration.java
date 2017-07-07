package com.github.vvinston.konfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class AdaptedConfiguration implements Configuration {

    private final Function<String, String> readAdapter;
    private final Function<String, Map<String, String>> findAdapter;

    public AdaptedConfiguration(
            final Function<String, String> readAdapter,
            final Function<String, Map<String, String>> findAdapter) {
        this.readAdapter = readAdapter;
        this.findAdapter = findAdapter;
    }

    @Override
    public Optional<ConfigValue> read(final String id) {
        final String result = readAdapter.apply(id);
        return result == null
                ? Optional.empty()
                : Optional.of(new StaticConfigValue(result));
    }

    @Override
    // There might be a more elegant solution for this
    public Map<String, ConfigValue> find(String prefix) {
        final Map<String, ConfigValue> result = new HashMap<>();
        for (final Map.Entry<String, String> record : findAdapter.apply(prefix).entrySet()) {
            result.put(record.getKey(), new StaticConfigValue(record.getValue()));
        }
        return result;
    }
}
