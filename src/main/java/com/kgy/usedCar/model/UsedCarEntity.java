package com.kgy.usedCar.model;

import com.kgy.usedCar.dto.request.car.UsedCarDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "used_car")
public class UsedCarEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model")
    private String model;

    @Column(name = "car_type")
    private String carType;

    @Column(name = "transmission")
    private String transmission;

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "engine")
    private int engine;

    @Column(name = "color")
    private String color;

    @Column(name = "model_year")
    private int modelYear;

    @Column(name = "mileage")
    private int mileage;

    @Column(name = "price")
    private int price;

    @Column(name = "discounted_price")
    private int discountedPrice;

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "is_hot_deal")
    private boolean isHotDeal = false;

    @Column(name = "view_count")
    private int viewCount;

    public static UsedCarEntity fromDto(UsedCarDto dto) {
        return UsedCarEntity.builder()
                .model(dto.getModel())
                .carType(dto.getCarType())
                .transmission(dto.getTransmission())
                .licensePlate(dto.getLicensePlate())
                .engine(dto.getEngine())
                .color(dto.getColor())
                .modelYear(dto.getModelYear())
                .mileage(dto.getMileage())
                .price(dto.getPrice())
                .discountedPrice(dto.getDiscountedPrice())
                .fuelType(dto.getFuelType())
                .isHotDeal(dto.isHotDeal())
                .viewCount(dto.getViewCount())
                .build();
    }

    public UsedCarEntity update(UsedCarDto dto) {
        this.model = dto.getModel();
        this.carType = dto.getCarType();
        this.transmission = dto.getTransmission();
        this.licensePlate = dto.getLicensePlate();
        this.engine = dto.getEngine();
        this.color = dto.getColor();
        this.modelYear = dto.getModelYear();
        this.mileage = dto.getMileage();
        this.price = dto.getPrice();
        this.discountedPrice = dto.getDiscountedPrice();
        this.fuelType = dto.getFuelType();
        this.isHotDeal = dto.isHotDeal();
        return this;
    }
}
