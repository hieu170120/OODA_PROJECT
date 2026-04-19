package com.foodorder.dto;

import com.foodorder.entity.Customer;

public class CustomerDTO {
    private String userId;
    private String fullName;
    private String email;
    private String role;

    public CustomerDTO() {
    }

    public static CustomerDTO fromEntity(Customer customer) {
        if (customer == null) return null;
        CustomerDTO dto = new CustomerDTO();
        dto.setUserId(customer.getUserId());
        dto.setFullName(customer.getFullName());
        dto.setEmail(customer.getEmail());
        dto.setRole("CUSTOMER");
        return dto;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
