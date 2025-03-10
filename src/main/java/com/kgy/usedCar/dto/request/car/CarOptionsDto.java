package com.kgy.usedCar.dto.request.car;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarOptionsDto {
    private boolean frontRearSensor;
    private boolean rearSensor;
    private boolean frontSensor;
    private boolean heatedSeat;
    private boolean ventilatedSeat;
    private boolean smartKey;
    private boolean navigation;
    private boolean ledHeadlight;
    private boolean sunroof;
}
