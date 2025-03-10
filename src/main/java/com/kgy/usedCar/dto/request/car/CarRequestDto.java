package com.kgy.usedCar.dto.request.car;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CarRequestDto {
    private CarOptionsDto carOptionsDto;
    private UsedCarDto usedCarDto;
    private List<String> imageTypes;
}
