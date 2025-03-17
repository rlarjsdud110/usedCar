package com.kgy.usedCar.dto.response.consult;

import com.kgy.usedCar.model.ConsultEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ConsultResponseDto {
    private String email;
    private String statusType;
    private String title;
    private String content;
    private List<String> imagesUrl;
    private LocalDateTime createdAt;
    private String answer;

    public static ConsultResponseDto fromEntity(ConsultEntity entity, List<String> imagesUrl){
        return new ConsultResponseDto(
                entity.getEmail(),
                entity.getStatusType(),
                entity.getTitle(),
                entity.getContent(),
                imagesUrl,
                entity.getCreatedAt(),
                entity.getAnswer()
        );
    }
}
