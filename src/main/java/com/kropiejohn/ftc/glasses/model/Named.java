package com.kropiejohn.ftc.glasses.model;

import java.util.Objects;

public interface Named {
    String getName();

    default <T extends Enum & Named> T getEnumFromName(String name) {
        var namedObjArr = this.getClass().getEnumConstants();
        for (Object namedObj : namedObjArr) {
            if (namedObj instanceof Named) {
                var named = (Named) namedObj;
                if (Objects.equals(named.getName(), name)) {
                    return (T) named;
                }
            }
        }

        return null;
    }
}
