package com.kgy.usedCar.dto.request.car;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsedCarDto {
    private String model;
    private String carType;
    private String transmission;
    private String licensePlate;
    private int engine;
    private String color;
    private int modelYear;
    private int mileage;
    private int price;
    private int discountedPrice;
    private String fuelType;
    private boolean isHotDeal = false;
    private int viewCount;
}
