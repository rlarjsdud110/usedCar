package com.kgy.usedCar.controller;

import com.kgy.usedCar.dto.response.Response;
import com.kgy.usedCar.dto.response.notice.NoticeResponseDto;
import com.kgy.usedCar.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;
    @GetMapping("/list")
    public Response<List<NoticeResponseDto>> getAllNotice(){
        List<NoticeResponseDto> noticeList = noticeService.getAllNotice();
        return Response.success(noticeList);
    }


}