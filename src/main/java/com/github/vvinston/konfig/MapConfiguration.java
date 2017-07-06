package com.github.vvinston.konfig;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class MapConfiguration implements Configuration {
    private final Map<String, String> configuration;

    public MapConfiguration(final Map<String, String> configuration) {
        this.configuration = Collections.unmodifiableMap(new HashMap<>(Objects.requireNonNull(configuration)));
    }

    @Override
    public Optional<ConfigValue> read(final String id) {
        return configuration.containsKey(id) ? Optional.of(new ReferenceConfigValue(id)) : Optional.empty();
    }

    @Override
    public Map<String, ConfigValue> find(final String prefix) {
        final Map<String, ConfigValue> result = new HashMap<>();

        for (final Map.Entry<String, String> record : configuration.entrySet()) {
            if (record.getKey().startsWith(prefix)) {
                result.put(record.getKey(), new ReferenceConfigValue(record.getValue()));
            }
        }

        return result;
    }

    private class ReferenceConfigValue implements ConfigValue {
        private final String id;

        private ReferenceConfigValue(String id) {
            this.id = id;
        }

        @Override
        public String asString() {
            return configuration.get(id);
        }

        @Override
        public Number asNumber() {
            return new ParsedNumber(configuration.get(id));
        }

        @Override
        public boolean asFlag() {
            return Boolean.parseBoolean(configuration.get(id));
        }

        @Override
        public <T> T as(final Function<String, T> mapper) {
            return mapper.apply(configuration.get(id));
        }
    }
}
