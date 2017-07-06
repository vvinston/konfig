package com.github.vvinston.konfig;

import java.util.function.Function;

final class StaticConfigValue implements ConfigValue {

    private final String value;

    StaticConfigValue(final String value) {
        this.value = value;
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public Number asNumber() {
        return new ParsedNumber(value);
    }

    @Override
    public boolean asFlag() {
        return Boolean.parseBoolean(value);
    }

    @Override
    public <T> T as(Function<String, T> mapper) {
        return mapper.apply(value);
    }
}
