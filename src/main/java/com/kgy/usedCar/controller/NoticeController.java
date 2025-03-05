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

    @PostMapping("/create")
    public Response<Void> createNotice(@RequestBody NoticeResponseDto dto){
        noticeService.createNotice(dto);
        return Response.success();
    }

    @PutMapping("/update/{id}")
    public Response<Void> updateNotice(@PathVariable Long id, @RequestBody NoticeResponseDto dto){
        noticeService.updateNotice(id, dto);
        return Response.success();
    }

    @DeleteMapping("/delete/{id}")
    public Response<Void> deleteNotice(@PathVariable Long id){
        noticeService.deleteNotice(id);
        return Response.success();
    }


}