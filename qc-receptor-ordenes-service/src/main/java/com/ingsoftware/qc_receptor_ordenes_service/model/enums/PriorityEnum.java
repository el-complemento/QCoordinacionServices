package com.ingsoftware.qc_receptor_ordenes_service.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PriorityEnum {
    ROUTINE("routine"),
    URGENT("urgent"),
    ASAP("asap");

    private final String value;

    PriorityEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PriorityEnum fromValue(String value) {
        for (PriorityEnum priority : PriorityEnum.values()) {
            if (priority.value.equalsIgnoreCase(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}