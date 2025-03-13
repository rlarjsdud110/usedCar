package com.kgy.usedCar.dto.response.consult;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ConsultListResponseDto {
    private Long id;
    private String title;
    private String statusType;
    private String taskType;
    private String email;
    private LocalDateTime createdAt;
}
