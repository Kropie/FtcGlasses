package com.kropiejohn.ftc.glasses.model;

/**
 * Gender enum.
 */
public enum YesNo implements Abbreviated {
    YES("Yes", "Y"),
    NO("No", "N");

    /**
     * Enum name.
     */
    private final String name;

    /**
     * Enum abbreviation.
     */
    private final String abbreviation;

    /**
     * Constructor.
     *
     * @param name         the name of the enum.
     * @param abbreviation the abbreviation of the enum.
     */
    YesNo(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    /**
     * Getter for the name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the abbreviation.
     *
     * @return the abbreviation.
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    public static YesNo fromBoolean(final boolean value) {
        if (value) {
            return YES;
        } else {
            return NO;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
