package io.bootify.vcore.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CommissionType {
    HEADSHOT("Headshot"),
    HALFBODY("Halfbody"),
    FULLBODY("Fullbody");

    private final String displayName;

    CommissionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static CommissionType fromValue(String value) {
        if (value == null) return null;
        for (CommissionType type : CommissionType.values()) {
            // Try matching by constant name (case-insensitive)
            if (type.name().equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value.replace("-", ""))) {
                return type;
            }
            // Try matching by display name (case-insensitive)
            if (type.displayName.equalsIgnoreCase(value) || type.displayName.equalsIgnoreCase(value.replace("-", ""))) {
                return type;
            }
            // Try matching with dash/underscore conversion
            if (type.name().replace("_", "-").equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}
