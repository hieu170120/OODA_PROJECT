package com.foodorder.uml.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "uml_staff")
public class Staff extends User {
    public Staff() {
    }

    public Staff(String id, String username, String password, String phone, String fullName) {
        super(id, username, password, phone, fullName);
    }
}
