package com.foodorder.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "dishes")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "dish_id")
    private String dishId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    public void updatePrice(Double newPrice) {
        this.price = newPrice;
    }

}