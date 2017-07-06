package com.github.vvinston.konfig;

import java.util.Map;
import java.util.Optional;

public interface Configuration {
    Optional<ConfigValue> read(String id);
    Map<String, ConfigValue> find(String prefix);
}
