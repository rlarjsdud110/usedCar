package com.kgy.usedCar.dto.response.car;

import com.kgy.usedCar.model.UsedCarEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class SearchResponseDto {
    private long id;
    private String model;
    private int modelYear;
    private int mileage;
    private String fuelType;
    private int price;
    private int discountedPrice;
    private boolean isHotDeal;
    private String imageUrl;

    public static SearchResponseDto fromEntity(UsedCarEntity entity, String imageUrl){
        return SearchResponseDto.builder()
                .id(entity.getId())
                .model(entity.getModel())
                .modelYear(entity.getModelYear())
                .mileage(entity.getMileage())
                .fuelType(entity.getFuelType())
                .price(entity.getPrice())
                .discountedPrice(entity.getDiscountedPrice())
                .isHotDeal(entity.isHotDeal())
                .imageUrl(imageUrl)
                .build();
    }
}
