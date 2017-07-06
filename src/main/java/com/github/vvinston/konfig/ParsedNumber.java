package com.github.vvinston.konfig;

import java.util.Objects;

final class ParsedNumber extends Number {

    private final String number;

    ParsedNumber(final String number) {
        this.number = Objects.requireNonNull(number);
    }

    @Override
    public int intValue() {
        return Integer.parseInt(number);
    }

    @Override
    public long longValue() {
        return Long.parseLong(number);
    }

    @Override
    public float floatValue() {
        return Float.parseFloat(number);
    }

    @Override
    public double doubleValue() {
        return Double.parseDouble(number);
    }
}
