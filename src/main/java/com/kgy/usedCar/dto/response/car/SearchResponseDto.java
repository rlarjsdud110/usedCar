package com.kgy.usedCar.dto.response.car;

import com.kgy.usedCar.model.UsedCarEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SearchResponseDto {
    private long id;
    private String model;
    private int modelYear;
    private int mileage;
    private String fuelType;
    private int price;
    private int discountedPrice;
    private boolean isHotDeal;

    public static SearchResponseDto fromEntity(UsedCarEntity entity){
        return new SearchResponseDto(
                entity.getId(),
                entity.getModel(),
                entity.getModelYear(),
                entity.getMileage(),
                entity.getFuelType(),
                entity.getPrice(),
                entity.getDiscountedPrice(),
                entity.isHotDeal()
        );
    }
}
