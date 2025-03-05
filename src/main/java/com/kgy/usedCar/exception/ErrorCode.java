package com.kgy.usedCar.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "이미 가입된 아이디 입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"비밀번호가 올바르지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원정보가 존재하지 않습니다."),
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}
