package com.blue.app.model;

public enum Schedule {
    From7PMto8PM("De 7:00 pm a 8:00 pm"),
    From8PMto9PM("De 8:00 pm a 9:00 pm");

    private final String displayName;

    Schedule(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
