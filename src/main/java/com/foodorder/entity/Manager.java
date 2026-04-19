package com.foodorder.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "managers")
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id")
public class Manager extends User {


}