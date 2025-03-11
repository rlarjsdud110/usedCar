package com.kgy.usedCar.dto.response.car;

import com.kgy.usedCar.model.UsedCarEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecommendCarDto {
    private Long id;
    private String model;
    private int price;
    private int discountedPrice;
    private boolean isHotDeal = false;
    private String imageUrl;

    public static RecommendCarDto fromEntity(UsedCarEntity entity, String imageUrl){
        return new RecommendCarDto(
                entity.getId(),
                entity.getModel(),
                entity.getPrice(),
                entity.getDiscountedPrice(),
                entity.isHotDeal(),
                imageUrl
        );
    }
}
