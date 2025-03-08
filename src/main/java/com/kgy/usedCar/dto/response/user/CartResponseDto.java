package com.kgy.usedCar.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartResponseDto {
    private Long id; // car table id
    private String model;
}
