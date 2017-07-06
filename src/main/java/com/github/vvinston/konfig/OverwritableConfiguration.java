package com.github.vvinston.konfig;

import java.util.Optional;

public interface OverwritableConfiguration extends Configuration {
    void change(String id, String value);
}
