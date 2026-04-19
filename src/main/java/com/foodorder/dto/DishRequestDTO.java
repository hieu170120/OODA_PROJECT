package com.foodorder.dto;

import com.foodorder.entity.Dish;

public class DishRequestDTO {
    private String name;
    private Double price;
    private String imageUrl;
    private String description;

    public DishRequestDTO() {
    }

    public Dish toEntity() {
        return Dish.builder()
                    .setName(this.name)
                    .setPrice(this.price)
                    .setImageUrl(this.imageUrl)
                    .setDescription(this.description)
                    .build();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
