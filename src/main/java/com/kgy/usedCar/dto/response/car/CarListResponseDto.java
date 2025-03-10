package com.kgy.usedCar.dto.response.car;

import com.kgy.usedCar.model.UsedCarEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CarListResponseDto {
    private long id;
    private String model;
    private int modelYear;
    private int mileage;
    private String fuelType;
    private int price;
    private int discountedPrice;
    private boolean isHotDeal;
    private String imagesUrls;

    public static CarListResponseDto fromEntity(UsedCarEntity entity, String imagesUrls){
        return CarListResponseDto.builder()
                .id(entity.getId())
                .model(entity.getModel())
                .modelYear(entity.getModelYear())
                .mileage(entity.getMileage())
                .fuelType(entity.getFuelType())
                .price(entity.getPrice())
                .discountedPrice(entity.getDiscountedPrice())
                .isHotDeal(entity.isHotDeal())
                .imagesUrls(imagesUrls)
                .build();
    }
}
