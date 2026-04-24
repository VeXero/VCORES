package io.bootify.vcore.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ArtstyleType {
    SKETCH("Sketch"),
    SIMPLE("Simple render"),
    RENDER("Render"),
    PAINTING("Painting");

    private final String displayName;

    ArtstyleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static ArtstyleType fromValue(String value) {
        if (value == null) return null;
        for (ArtstyleType type : ArtstyleType.values()) {
            if (type.name().equalsIgnoreCase(value) || type.displayName.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}
