package com.kgy.usedCar.dto.response.car;

import com.kgy.usedCar.model.UsedCarEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HotDealResponseDto {
    private long id;
    private String model;
    private int modelYear;
    private int mileage;
    private String fuelType;
    private int price;
    private int discountedPrice;

    public static HotDealResponseDto fromEntity(UsedCarEntity entity){
        return new HotDealResponseDto(
                entity.getId(),
                entity.getModel(),
                entity.getModelYear(),
                entity.getMileage(),
                entity.getFuelType(),
                entity.getPrice(),
                entity.getDiscountedPrice()
        );
    }
}
