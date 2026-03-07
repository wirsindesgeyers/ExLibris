package com.biblioteca_api.biblioteca.entities;

public enum UserRole {

    ADMIN("admin"),

    LIBRARIAN("librarian"),

    READER("reader");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
