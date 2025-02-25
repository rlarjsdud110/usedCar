package com.kgy.usedCar.dto.request.user;

import lombok.Getter;

@Getter
public class UserSignupRequest {
    private String userId;

    private String password;

    private String email;

    private String name;

    private String phone;
}
