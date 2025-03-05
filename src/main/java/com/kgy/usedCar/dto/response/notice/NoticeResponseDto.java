package com.kgy.usedCar.dto.response.notice;

import com.kgy.usedCar.model.NoticeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NoticeResponseDto {
    private Long id;
    private String title;
    private String content;


    public static NoticeResponseDto fromEntity(NoticeEntity noticeEntity){
        return new NoticeResponseDto(
                noticeEntity.getId(),
                noticeEntity.getTitle(),
                noticeEntity.getContent()
        );
    }
}
