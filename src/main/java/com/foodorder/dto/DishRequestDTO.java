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
        Dish dish = new Dish();
        dish.setName(this.name);
        dish.setPrice(this.price);
        dish.setImageUrl(this.imageUrl);
        dish.setDescription(this.description);
        return dish;
    }
}
