package com.kgy.usedCar.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PurchaseListResponseDto {
    private String userName;
    private String phone;
    private Long carId;
    private Long purchaseId;
    private LocalDateTime createdAt;

    public static PurchaseListResponseDto of(String userName, String phone, Long carId, Long purchaseId, LocalDateTime createdAt){
        return new PurchaseListResponseDto(
                userName, phone, carId, purchaseId, createdAt);
    }
}
