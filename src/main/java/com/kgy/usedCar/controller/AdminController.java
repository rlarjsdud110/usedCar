package com.kgy.usedCar.controller;

import com.kgy.usedCar.dto.response.Response;
import com.kgy.usedCar.dto.response.notice.NoticeResponseDto;
import com.kgy.usedCar.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final NoticeService noticeService;

    @PostMapping("/notice")
    public Response<Void> createNotice(@RequestBody NoticeResponseDto dto) {
        noticeService.createNotice(dto);
        return Response.success();
    }

    @PutMapping("/notice/{id}")
    public Response<Void> updateNotice(@PathVariable Long id, @RequestBody NoticeResponseDto dto){
        noticeService.updateNotice(id, dto);
        return Response.success();
    }

    @DeleteMapping("/notice/{id}")
    public Response<Void> deleteNotice(@PathVariable Long id){
        noticeService.deleteNotice(id);
        return Response.success();
    }
}
