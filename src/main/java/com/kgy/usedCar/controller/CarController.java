package com.kgy.usedCar.controller;

import com.kgy.usedCar.dto.response.Response;
import com.kgy.usedCar.dto.response.car.HotDealResponseDto;
import com.kgy.usedCar.dto.response.car.RankingResponseDto;
import com.kgy.usedCar.dto.response.car.SearchResponseDto;
import com.kgy.usedCar.service.UsedCarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/rankings")
    public Response<RankingResponseDto> getRankings(){
        RankingResponseDto rankings = usedCarService.getRankings();
        return Response.success(rankings);
    }

    @GetMapping("/search")
    public Response<List<SearchResponseDto>> searchCars(@RequestParam String searchName){
        List<SearchResponseDto> searchCarList = usedCarService.searchCars(searchName);
        return Response.success(searchCarList);
    }
}
