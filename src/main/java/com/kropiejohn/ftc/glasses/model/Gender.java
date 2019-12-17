package com.kropiejohn.ftc.glasses.model;

/**
 * Gender enum.
 */
public enum Gender implements Abbreviated {
    MALE("Male", "M"),
    FEMALE("Female", "F"),
    UNISEX("Unisex", "U");

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
    Gender(String name, String abbreviation) {
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

    @Override
    public String toString() {
        return name;
    }
}
