package com.foodorder.model;

import java.time.LocalDateTime;

public class User {
    private String userId;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private LocalDateTime createdAt;
    private Address address;

    public User() {
    }

    public User(String userId, String fullName, String email, String password, String phone, LocalDateTime createdAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public User(String userId, String fullName, String email, String password, String phone, LocalDateTime createdAt, Address address) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.createdAt = createdAt;
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
