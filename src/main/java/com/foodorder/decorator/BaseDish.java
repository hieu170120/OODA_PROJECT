package com.foodorder.decorator;

public class BaseDish implements IDish {
    private String dishId;
    private String name;
    private double basePrice;
    private String imageUrl;
    private String description;

    public BaseDish() {
    }

    public BaseDish(String dishId, String name, double basePrice, String imageUrl, String description) {
        this.dishId = dishId;
        this.name = name;
        this.basePrice = basePrice;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return basePrice;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
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
