package com.ingsoftware.qc_receptor_ordenes_service.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusEnum {
    COMPLETED("completed"),
    ACTIVE("active"),
    ON_HOLD("on-hold");

    private final String value;

    StatusEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}