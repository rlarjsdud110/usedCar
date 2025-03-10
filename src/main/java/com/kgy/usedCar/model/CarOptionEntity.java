package com.kgy.usedCar.model;

import com.kgy.usedCar.dto.request.car.CarOptionsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "car_options")
public class CarOptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "front_rear_sensor")
    private boolean frontRearSensor;

    @Column(name = "rear_sensor")
    private boolean rearSensor;

    @Column(name = "front_sensor")
    private boolean frontSensor;

    @Column(name = "heated_seat")
    private boolean heatedSeat;

    @Column(name = "ventilated_seat")
    private boolean ventilatedSeat;

    @Column(name = "smart_key")
    private boolean smartKey;

    @Column(name = "navigation")
    private boolean navigation;

    @Column(name = "led_headlight")
    private boolean ledHeadlight;

    @Column(name = "sunroof")
    private boolean sunroof;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_car_id")
    private UsedCarEntity usedCar;

    public static CarOptionEntity fromDto(CarOptionsDto dto, UsedCarEntity entity){
        return CarOptionEntity.builder()
                .frontRearSensor(dto.isFrontRearSensor())
                .rearSensor(dto.isRearSensor())
                .frontSensor(dto.isFrontSensor())
                .heatedSeat(dto.isHeatedSeat())
                .ventilatedSeat(dto.isVentilatedSeat())
                .smartKey(dto.isSmartKey())
                .navigation(dto.isNavigation())
                .ledHeadlight(dto.isLedHeadlight())
                .sunroof(dto.isSunroof())
                .usedCar(entity)
                .build();
    }
}
