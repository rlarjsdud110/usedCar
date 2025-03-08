package com.kgy.usedCar.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateRequestDto {
    private String name;
    private String email;
    private String phone;
}
