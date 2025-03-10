package com.kgy.usedCar.dto.response.car;

import com.kgy.usedCar.model.UsedCarEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class HotDealResponseDto {
    private long id;
    private String model;
    private int modelYear;
    private int mileage;
    private String fuelType;
    private int price;
    private int discountedPrice;
    private String imagesUrls;

    public static HotDealResponseDto fromEntity(UsedCarEntity entity, String imagesUrls){
        return HotDealResponseDto.builder()
                .id(entity.getId())
                .model(entity.getModel())
                .modelYear(entity.getModelYear())
                .mileage(entity.getMileage())
                .fuelType(entity.getFuelType())
                .price(entity.getPrice())
                .discountedPrice(entity.getDiscountedPrice())
                .imagesUrls(imagesUrls)
                .build();
    }

}
