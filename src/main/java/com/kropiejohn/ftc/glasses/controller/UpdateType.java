package com.kropiejohn.ftc.glasses.controller;

public enum UpdateType {
    CREATE("Add"),
    UPDATE("Edit");

    private final String name;

    UpdateType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
