package com.kgy.usedCar.controller;

import com.kgy.usedCar.dto.request.admin.AdminConsultRequestDto;
import com.kgy.usedCar.dto.request.consult.ConsultRequestDto;
import com.kgy.usedCar.dto.response.Response;
import com.kgy.usedCar.dto.response.consult.ConsultListResponseDto;
import com.kgy.usedCar.dto.response.notice.NoticeResponseDto;
import com.kgy.usedCar.service.AdminService;
import com.kgy.usedCar.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final NoticeService noticeService;
    private final AdminService adminService;

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

    @GetMapping("/consult")
    public Response<List<ConsultListResponseDto>> consultList(){
        List<ConsultListResponseDto> consultList = adminService.consultList();
        return Response.success(consultList);
    }

    @PatchMapping("/consult/{consultId}")
    public Response<Void> consultAnswer(@PathVariable Long consultId, @RequestBody AdminConsultRequestDto dto){
        adminService.consultAnswer(consultId, dto);
        return Response.success();
    }


}
