package com.kropiejohn.ftc.glasses.model;

import java.util.Objects;

public interface Abbreviated extends Named {
    String getAbbreviation();

    default <T extends Enum & Abbreviated> T getEnumFromAbbreviation(String abbreviation) {
        var abbreviatedObjArr = this.getClass().getEnumConstants();
        for (Object abbreviatedObj : abbreviatedObjArr) {
            if (abbreviatedObj instanceof Abbreviated) {
                var abbreviated = (Abbreviated) abbreviatedObj;
                if (Objects.equals(abbreviated.getAbbreviation(), abbreviation)) {
                    return (T) abbreviated;
                }
            }
        }

        return null;
    }

    default <T extends Enum & Abbreviated> T getEnumFromAbbreviationOrName(String nameOrAbbreviation) {
        var value = getEnumFromAbbreviation(nameOrAbbreviation);
        if (value == null) {
            return getEnumFromName(nameOrAbbreviation);
        } else {
            return (T) value;
        }
    }
}
