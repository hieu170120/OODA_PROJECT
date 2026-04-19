package com.foodorder.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "staffs")
@PrimaryKeyJoinColumn(name = "staff_id")
public class Staff extends User {


    public Staff() {
    }
}