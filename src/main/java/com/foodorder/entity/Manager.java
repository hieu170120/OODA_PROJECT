package com.foodorder.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "managers")
@PrimaryKeyJoinColumn(name = "manager_id") // Khóa chính của bảng này sẽ nối với Khóa chính bảng users
@Data
@EqualsAndHashCode(callSuper = true)
public class Manager extends User {
    
}