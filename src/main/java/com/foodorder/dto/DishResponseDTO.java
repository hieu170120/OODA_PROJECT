package com.foodorder.dto;

import com.foodorder.entity.Dish;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DishResponseDTO {
    private String dishId;
    private String name;
    private Double price;
    private String imageUrl;
    private String description;

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
}
