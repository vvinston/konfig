package com.github.vvinston.konfig;

import java.util.Optional;

public interface OverwritableConfiguration extends Configuration {
    void overwrite(String id, String value);
}
