package com.kgy.usedCar.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserSignupRequest {
    private String userId;

    private String password;

    private String email;

    private String name;

    private String phone;
}
