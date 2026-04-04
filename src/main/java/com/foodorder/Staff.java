package com.foodorder;

import java.time.LocalDateTime;

public class Staff extends User {
    private String role;

    public Staff() {
    }

    public Staff(String userId, String fullName, String email, String password, String phone, LocalDateTime createdAt, String role) {
        super(userId, fullName, email, password, phone, createdAt);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
