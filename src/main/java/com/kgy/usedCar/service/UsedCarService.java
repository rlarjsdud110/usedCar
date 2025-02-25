package com.kgy.usedCar.service;

import com.kgy.usedCar.dto.response.car.HotDealResponseDto;
import com.kgy.usedCar.model.UsedCarEntity;
import com.kgy.usedCar.repository.UsedCarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
