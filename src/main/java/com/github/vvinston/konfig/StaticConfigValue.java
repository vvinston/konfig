package com.github.vvinston.konfig;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaticConfigValue that = (StaticConfigValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
