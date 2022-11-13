package com.Chatapi.Enums;

public enum Permission {
    READ_WRITE("read&write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}