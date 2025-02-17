package com.kgy.usedCar.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UsedCarException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;

    @Override
    public String getMessage(){
        if(message == null){
            return errorCode.getMessage();
        }

        return String.format("%s. %s", errorCode.getMessage(), message);
    }
}
