package com.github.vvinston.konfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public final class SmartConfiguration  implements Configuration {

    private final Configuration configuration;
    private final Predicate<ConfigValue> predicate;
    private final Function<ConfigValue, String> keyFactory;

    public SmartConfiguration(
            final Configuration configuration,
            final Predicate<ConfigValue> predicate,
            final Function<ConfigValue, String> keyFactory) {
        this.configuration = configuration;
        this.predicate = predicate;
        this.keyFactory = keyFactory;
    }

    @Override
    public Optional<ConfigValue> read(final String id) {
        return read(id, new HashSet<>());
    }

    private Optional<ConfigValue> read(final String id, final Set<String> alreadyReadKeys) {
        final Optional<ConfigValue> result = configuration.read(id);
        if (result.isPresent() && predicate.test(result.get())) {
            alreadyReadKeys.add(id);
            final String nextKey = keyFactory.apply(result.get());
            if (alreadyReadKeys.contains(nextKey)) {
                throw new IllegalStateException("Incorrect configuration, contains circular references!");
            }
            return read(nextKey, alreadyReadKeys);
        } else {
            return result;
        }
    }

    @Override
    public Map<String, ConfigValue> find(final String prefix) {
        final Map<String, ConfigValue> result = new HashMap<>();
        for (final Map.Entry<String, ConfigValue> record : configuration.find(prefix).entrySet()) {
            final Optional<ConfigValue> candidate = predicate.test(record.getValue())
                    ? read(keyFactory.apply(record.getValue()))
                    : Optional.of(record.getValue());
            if (candidate.isPresent()) {
                result.put(record.getKey(), candidate.get());
            }
        }
        return result;
    }
}
