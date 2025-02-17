package com.kgy.usedCar.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "이미 가입된 아이디 입니다."),;

    private HttpStatus status;
    private String message;
}
