package com.kgy.usedCar.dto.response.user;

import com.kgy.usedCar.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginResponseDto {
    private String token;
    private UserRole role;
}
