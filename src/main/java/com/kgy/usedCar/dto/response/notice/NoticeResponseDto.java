package com.kgy.usedCar.dto.response.notice;

import com.kgy.usedCar.model.NoticeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class NoticeResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;


    public static NoticeResponseDto fromEntity(NoticeEntity noticeEntity){
        return new NoticeResponseDto(
                noticeEntity.getId(),
                noticeEntity.getTitle(),
                noticeEntity.getContent(),
                noticeEntity.getCreatedAt()
        );
    }
}
