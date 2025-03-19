package com.kgy.usedCar.controller;

import com.kgy.usedCar.dto.request.admin.AdminConsultRequestDto;
import com.kgy.usedCar.dto.request.car.CarRequestDto;
import com.kgy.usedCar.dto.response.Response;
import com.kgy.usedCar.dto.response.admin.DashboardStatsDTO;
import com.kgy.usedCar.dto.response.admin.PurchaseListResponseDto;
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

    @GetMapping("/dashboard")
    public Response<DashboardStatsDTO> dashboard(){
        DashboardStatsDTO dto = adminService.dashboard();
        return Response.success(dto);
    }

    @DeleteMapping("/car/{carId}")
    public Response<Void> deleteCar(@PathVariable Long carId){
        adminService.deleteCar(carId);
        return Response.success();
    }

    @PostMapping("/car/create")
    public Response<Void> createCar(@RequestPart("dto") CarRequestDto dto, @RequestPart(value = "multipartFiles", required = false) MultipartFile[] multipartFiles){
        adminService.createCar(dto, multipartFiles);
        return Response.success();
    }

    @PutMapping("/car/update/{carId}")
    public Response<Void> updatedCar(@PathVariable Long carId, @RequestPart("dto") CarRequestDto dto, @RequestPart(value = "multipartFiles", required = false) MultipartFile[] multipartFiles){
        adminService.updatedCar(carId, dto, multipartFiles);
        return Response.success();
    }

    @GetMapping("/purchase")
    public Response<List<PurchaseListResponseDto>> purchaseList(){
        List<PurchaseListResponseDto> purchaseListResponse = adminService.purchaseList();
        return Response.success(purchaseListResponse);
    }

    @DeleteMapping("/purchase/{purchaseId}")
    public Response<Void> purchaseDelete(@PathVariable Long purchaseId){
        adminService.purchaseDelete(purchaseId);
        return Response.success();
    }
}
