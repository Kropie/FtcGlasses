package com.kropiejohn.ftc.glasses.util;

import javafx.util.converter.NumberStringConverter;

public class EnhancedNumberStringConverter extends NumberStringConverter {
    @Override
    public Number fromString(String value) {
        if ("-".equals(value) || "-.".equals(value) || ".".equals(value)) {
            return 0;
        }
        return super.fromString(value);
    }
}
