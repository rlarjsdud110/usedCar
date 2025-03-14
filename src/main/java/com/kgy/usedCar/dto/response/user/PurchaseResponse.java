package com.kgy.usedCar.dto.response.user;

import com.kgy.usedCar.model.PurchaseRequestEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PurchaseResponse {
    Long id;
    String model;
    LocalDateTime createdAt;

    public static PurchaseResponse fromEntity(PurchaseRequestEntity entity){
        return PurchaseResponse.builder()
                .id(entity.getId())
                .model(entity.getUsedCar().getModel())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
