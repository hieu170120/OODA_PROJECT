package com.foodorder.dto;

import com.foodorder.entity.Dish;

public class DishResponseDTO {
    private String dishId;
    private String name;
    private Double price;
    private String imageUrl;
    private String description;

    public DishResponseDTO() {
    }

    public static DishResponseDTO fromEntity(Dish dish) {
        if (dish == null) return null;
        DishResponseDTO dto = new DishResponseDTO();
        dto.setDishId(dish.getDishId());
        dto.setName(dish.getName());
        dto.setPrice(dish.getPrice());
        dto.setImageUrl(dish.getImageUrl());
        dto.setDescription(dish.getDescription());
        return dto;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
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
