package com.kgy.usedCar.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
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

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "is_hot_deal")
    private boolean isHotDeal = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
