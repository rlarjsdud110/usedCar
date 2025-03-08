package com.kgy.usedCar.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ConsultResponseDto {
    private Long id;
    private String title;
    private String statusType;
    private String taskType;
    private LocalDateTime createdAt;
}
