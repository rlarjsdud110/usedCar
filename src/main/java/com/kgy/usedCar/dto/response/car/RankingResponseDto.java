package com.kgy.usedCar.dto.response.car;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RankingResponseDto {
    private List<CarRankingDto> viewRankings;
    private List<CarRankingDto> recentRankings;
}
