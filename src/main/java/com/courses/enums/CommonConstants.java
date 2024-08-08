package com.courses.enums;

import lombok.Getter;

@Getter
public enum CommonConstants {
    ACTIVATED("ACTIVATED"),
    INACTIVATED("DISABLED");

    private final String description;

    CommonConstants(String description){
        this.description = description;
    }
}
