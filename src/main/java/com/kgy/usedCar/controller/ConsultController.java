package com.kgy.usedCar.controller;

import com.kgy.usedCar.dto.request.consult.ConsultRequestDto;
import com.kgy.usedCar.dto.response.Response;
import com.kgy.usedCar.dto.response.consult.ConsultListResponseDto;
import com.kgy.usedCar.dto.response.consult.ConsultResponseDto;
import com.kgy.usedCar.service.ConsultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consult")
public class ConsultController {

    private final ConsultService consultService;

    @PostMapping
    public Response<Void> consultRequest(Principal principal, @RequestPart ConsultRequestDto dto,
                                         @RequestPart(value = "file", required = false) MultipartFile[] multipartFile){
        consultService.consultRequest(principal.getName(), dto, multipartFile);
        return Response.success();
    }

    @GetMapping("/{consultId}")
    public Response<ConsultResponseDto> consult(@PathVariable Long consultId){
        ConsultResponseDto consultList = consultService.consult(consultId);
        return Response.success(consultList);
    }

    @GetMapping("/list")
    public Response<List<ConsultListResponseDto>> consultList(Principal principal){
        List<ConsultListResponseDto> consultList = consultService.consultList(principal.getName());
        return Response.success(consultList);
    }

    @PutMapping("/{consultId}")
    public Response<Void> consultUpdate(@PathVariable Long consultId, @RequestPart ConsultRequestDto dto,
                                        @RequestPart(value = "file", required = false) MultipartFile[] multipartFile){
        consultService.consultUpdate(consultId, dto, multipartFile);
        return Response.success();
    }

    @DeleteMapping("/{consultId}")
    public Response<Void> consultDelete(@PathVariable Long consultId){
        consultService.consultDelete(consultId);
        return Response.success();
    }

}
