package com.kgy.usedCar.controller;

import com.kgy.usedCar.dto.request.car.CarRequestDto;
import com.kgy.usedCar.dto.response.Response;
import com.kgy.usedCar.dto.response.car.*;
import com.kgy.usedCar.service.UsedCarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cars")
public class UsedCarController {

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
    public Response<Page<SearchResponseDto>> searchCars(Pageable pageable, @RequestParam(required = false) String search){
        Page<SearchResponseDto> searchCarList = usedCarService.searchCars(pageable, search);
        return Response.success(searchCarList);
    }

    @GetMapping("/detail/{carId}")
    public Response<CarDetailResponseDto> carDetail(@PathVariable Long carId){
        CarDetailResponseDto carDetail = usedCarService.carDetail(carId);
        return Response.success(carDetail);
    }

    @GetMapping("/recommend")
    public Response<List<RecommendCarDto>> recommendCar() {
        List<RecommendCarDto> recommendCarDto = usedCarService.recommendCar();
        return Response.success(recommendCarDto);
    }

    @GetMapping("/carList")
    public Response<Page<CarListResponseDto>> carList(Pageable pageable){
        Page<CarListResponseDto> carList = usedCarService.carList(pageable);
        return Response.success(carList);
    }

}
