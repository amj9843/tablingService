package com.zerobase.tabling.data.constant;

public enum UserRole {
    //일반 사용자
    USER("일반 사용자"),

    //파트너 사용자
    PARTNER("파트너 사용자");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
