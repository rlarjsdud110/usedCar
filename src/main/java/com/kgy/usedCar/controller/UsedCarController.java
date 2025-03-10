package com.kgy.usedCar.controller;

import com.kgy.usedCar.dto.request.car.CarRequestDto;
import com.kgy.usedCar.dto.response.Response;
import com.kgy.usedCar.dto.response.car.CarDetailResponseDto;
import com.kgy.usedCar.dto.response.car.HotDealResponseDto;
import com.kgy.usedCar.dto.response.car.RankingResponseDto;
import com.kgy.usedCar.dto.response.car.SearchResponseDto;
import com.kgy.usedCar.service.UsedCarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cars")
public class UsedCarController {

    private final UsedCarService usedCarService;

    @PostMapping("/create")
    public Response<Void> createCar(@RequestPart("dto") CarRequestDto dto, @RequestPart("multipartFiles") MultipartFile[] multipartFiles){
        usedCarService.createCar(dto, multipartFiles);
        return Response.success();
    }

    @DeleteMapping("/delete/{carId}")
    public Response<Void> delete(@PathVariable Long carId){
        usedCarService.delete(carId);
        return Response.success();
    }

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

    @GetMapping("/detail/{carId}")
    public Response<CarDetailResponseDto> carDetail(@PathVariable Long carId){
        CarDetailResponseDto carDetail = usedCarService.carDetail(carId);
        return Response.success(carDetail);
    }
}
