package com.foodorder;

import java.time.LocalDateTime;

public class Manager extends User {
    private String managementLevel;

    public Manager() {
    }

    public Manager(String userId, String fullName, String email, String password, String phone, LocalDateTime createdAt, String managementLevel) {
        super(userId, fullName, email, password, phone, createdAt);
        this.managementLevel = managementLevel;
    }

    public String getManagementLevel() {
        return managementLevel;
    }

    public void setManagementLevel(String managementLevel) {
        this.managementLevel = managementLevel;
    }
}
