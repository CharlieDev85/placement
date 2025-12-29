package com.blue.app.model;

public enum Result {
    A1("A1"),
    A2("A2"),
    B1("B1"),
    B2("B2");

    private final String displayName;

    Result(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}
