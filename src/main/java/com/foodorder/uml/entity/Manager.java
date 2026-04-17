package com.foodorder.uml.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "uml_managers")
public class Manager extends User {
    public Manager() {
    }

    public Manager(String id, String username, String password, String phone, String fullName) {
        super(id, username, password, phone, fullName);
    }
}
