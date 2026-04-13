package com.foodorder.dto;

import com.foodorder.entity.Dish;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DishRequestDTO {
    private String name;
    private Double price;
    private String imageUrl;
    private String description;

    public Dish toEntity() {
        return Dish.builder()
                    .setName(this.name)
                    .setPrice(this.price)
                    .setImageUrl(this.imageUrl)
                    .setDescription(this.description)
                    .build();
    }
}
