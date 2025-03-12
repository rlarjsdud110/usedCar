package com.kgy.usedCar.dto.request.consult;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConsultRequestDto {
    private String email;
    private String statusType;
    private String title;
    private String content;
}
