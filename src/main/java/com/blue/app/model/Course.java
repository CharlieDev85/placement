package com.blue.app.model;

public enum Course {
    Basic1("Basic 1"),
    Basic2("Basic 2"),
    Basic3("Basic 3"),
    Basic4("Basic 4"),
    Basic5("Basic 5"),
    Intermediate1("Intermediate 1"),
    Intermediate2("Intermediate 2"),
    Intermediate3("Intermediate 3"),
    Intermediate4("Intermediate 4"),
    Intermediate5("Intermediate 5"),
    Advanced1("Advanced 1"),
    Advanced2("Advanced 2"),
    Advanced3("Advanced 3"),
    Conversation1("Conversation 1"),
    Conversation2("Conversation 2");

    private final String displayName;

    Course(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
