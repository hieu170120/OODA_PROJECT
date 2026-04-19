package com.foodorder.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "dishes")


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

    private Dish(DishBuilder builder) {
        this.dishId = builder.dishId;
        this.name = builder.name;
        this.price = builder.price;
        this.imageUrl = builder.imageUrl;
        this.description = builder.description;
    }

    public static DishBuilder builder() {
        return new DishBuilder();
    }


    public static class DishBuilder {
        private String dishId;
        private String name;
        private Double price;
        private String imageUrl;
        private String description;

        public DishBuilder setDishId(String dishId) {
            this.dishId = dishId;
            return this;
        }

        public DishBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public DishBuilder setPrice(Double price) {
            this.price = price;
            return this;
        }

        public DishBuilder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public DishBuilder setDescription(String description) {
            this.description = description;
            return this;
        }
        
        public Dish build() {
            return new Dish(this);
        }
    }


    public Dish() {
    }

    public String getDishId() {
        return dishId;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}