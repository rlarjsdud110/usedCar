package com.kgy.usedCar.dto.request.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NoticeRequestDto {
    private String title;
    private String content;
}
