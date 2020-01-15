package com.teamE.users;

public enum UserRoleType {
    USER("USER"),
    KEYHOLDER("KEYHOLDER"),
    ADMIN("ADMIN");

    private String type;

    UserRoleType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
