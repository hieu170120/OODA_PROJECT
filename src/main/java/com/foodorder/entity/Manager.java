package com.foodorder.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "managers")
@PrimaryKeyJoinColumn(name = "manager_id")
public class Manager extends User {



    public Manager() {
    }
}