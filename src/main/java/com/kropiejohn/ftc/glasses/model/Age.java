package com.kropiejohn.ftc.glasses.model;

/**
 * Defines age enums.
 */
public enum Age implements Abbreviated {
    CHILD("Child", "C"),
    ADULT("Adult", "A");

    /**
     * The name.
     */
    private final String name;

    /**
     * The abbreviation.
     */
    private final String abbreviation;

    /**
     * Constructor.
     *
     * @param name         the name.
     * @param abbreviation the abbreviation.
     */
    Age(String name, String abbreviation) {
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
