package com.kgy.usedCar.service;

import com.kgy.usedCar.dto.response.car.CarRankingDto;
import com.kgy.usedCar.dto.response.car.HotDealResponseDto;
import com.kgy.usedCar.dto.response.car.RankingResponseDto;
import com.kgy.usedCar.dto.response.car.SearchResponseDto;
import com.kgy.usedCar.exception.ErrorCode;
import com.kgy.usedCar.exception.UsedCarException;
import com.kgy.usedCar.model.UsedCarEntity;
import com.kgy.usedCar.repository.UsedCarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UsedCarService {

    private final UsedCarRepository usedCarRepository;

    public List<HotDealResponseDto> getHotDeals(){
        List<UsedCarEntity> usedCarEntity = usedCarRepository.findAll();

        if(usedCarEntity.isEmpty()){
            return Collections.emptyList();
        }

        //TODO AWS 이미지 가져오기
        return usedCarEntity.stream()
                .map(HotDealResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public RankingResponseDto getRankings() {
        // 조회순위
        List<UsedCarEntity> viewRankings = usedCarRepository.findTop5ByOrderByViewCountDesc();
        // 최신등록순위
        List<UsedCarEntity> recentRankings = usedCarRepository.findTop5ByOrderByCreatedAtDesc();

        List<CarRankingDto> viewRankingDto = viewRankings.stream()
                .map(car -> new CarRankingDto(car.getId(), car.getModel()))
                .toList();

        List<CarRankingDto> recentRankingDto = recentRankings.stream()
                .map(car -> new CarRankingDto(car.getId(), car.getModel()))
                .toList();

        return new RankingResponseDto(viewRankingDto, recentRankingDto);
    }

    public List<SearchResponseDto> searchCars(String searchName){
        if(searchName.length() < 2){
            throw new UsedCarException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        List<UsedCarEntity> usedCarEntity = usedCarRepository.findByModelContaining(searchName);

        return usedCarEntity.stream()
                .map(SearchResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
