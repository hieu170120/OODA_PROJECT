package com.foodorder.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "managers")
@PrimaryKeyJoinColumn(name = "manager_id")
@Data
@EqualsAndHashCode(callSuper = true)
public class Manager extends User {
    
}