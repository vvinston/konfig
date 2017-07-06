package com.github.vvinston.konfig;

import java.util.function.Function;

public interface ConfigValue {
    String asString();
    Number asNumber();
    boolean asFlag();
    <T> T as(Function<String, T> mapper);
}
