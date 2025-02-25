package com.kgy.usedCar.dto.request.user;

import lombok.Getter;

@Getter
public class UserLoginRequest {
    private String userId;
    private String password;
}
