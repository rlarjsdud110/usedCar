package com.kgy.usedCar.controller;

import com.kgy.usedCar.dto.response.Response;
import com.kgy.usedCar.dto.response.car.HotDealResponseDto;
import com.kgy.usedCar.service.UsedCarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cars")
public class CarController {

    private final UsedCarService usedCarService;

    @GetMapping("/hotDeals")
    public Response<List<HotDealResponseDto>> getHotDeals(){
        List<HotDealResponseDto> hotDealResponseList = usedCarService.getHotDeals();
        return Response.success(hotDealResponseList);
    }
}
