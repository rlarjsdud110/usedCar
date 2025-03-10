package com.kgy.usedCar.service;

import com.kgy.usedCar.dto.request.car.CarRequestDto;
import com.kgy.usedCar.dto.response.car.*;
import com.kgy.usedCar.exception.ErrorCode;
import com.kgy.usedCar.exception.UsedCarException;
import com.kgy.usedCar.model.CarImageEntity;
import com.kgy.usedCar.model.CarOptionEntity;
import com.kgy.usedCar.model.UsedCarEntity;
import com.kgy.usedCar.model.UserEntity;
import com.kgy.usedCar.repository.CarImageRepository;
import com.kgy.usedCar.repository.CarOptionRepository;
import com.kgy.usedCar.repository.UsedCarRepository;
import com.kgy.usedCar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UsedCarService {

    private final UsedCarRepository usedCarRepository;
    private final CarOptionRepository carOptionRepository;
    private final UserRepository userRepository;
    private final CarImageRepository carImageRepository;

    private final S3Service s3Service;

    @Transactional
    public void createCar(CarRequestDto dto, MultipartFile[] multipartFiles){
        try {
            UserEntity userEntity = userRepository.findById(dto.getUsedCarDto().getUserId())
                    .orElseThrow(() -> new UsedCarException(ErrorCode.USER_NOT_FOUND));

            UsedCarEntity usedCarEntity = UsedCarEntity.fromDto(dto.getUsedCarDto(), userEntity);
            UsedCarEntity usedCar = usedCarRepository.save(usedCarEntity);

            CarOptionEntity carOptionEntity = CarOptionEntity.fromDto(dto.getCarOptionsDto(), usedCar);
            carOptionRepository.save(carOptionEntity);

            s3Service.uploadImage(multipartFiles, usedCar.getId(), dto.getImageTypes());

        } catch (IOException e) {
            throw new UsedCarException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Transactional
    public void delete(Long carId){
        UsedCarEntity usedCarEntity = usedCarRepository.findById(carId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.CAR_NOT_FOUND));

        boolean result = s3Service.deleteImage(carId);

        if(result){
            usedCarRepository.delete(usedCarEntity);
        }
    }

    public List<HotDealResponseDto> getHotDeals() {
        List<UsedCarEntity> usedCarEntities = usedCarRepository.findByIsHotDealTrue();

        if (usedCarEntities.isEmpty()) {
            return Collections.emptyList();
        }

        List<HotDealResponseDto> hotDealResponseDto = new ArrayList<>();

        for (UsedCarEntity entity : usedCarEntities) {
            String imageUrl = getImageUrl(entity.getId());
            hotDealResponseDto.add(HotDealResponseDto.fromEntity(entity, imageUrl));
        }
        return hotDealResponseDto;
    }


    public RankingResponseDto getRankings() {

        List<UsedCarEntity> viewRankings = usedCarRepository.findTop5ByOrderByViewCountDesc();

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
        List<UsedCarEntity> usedCarEntity = usedCarRepository.findByModelContaining(searchName);

        return usedCarEntity.stream()
                .map(SearchResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public CarDetailResponseDto carDetail(Long carId){
        UsedCarEntity usedCarEntity = usedCarRepository.findById(carId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.CAR_NOT_FOUND));

        CarOptionEntity carOptionsEntity = carOptionRepository.findByUsedCar_Id(carId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.CAR_NOT_FOUND));

        usedCarEntity.setViewCount(usedCarEntity.getViewCount() + 1);
        usedCarRepository.save(usedCarEntity);

        List<String> carImagesUrl = getImageUrls(carId);

        return CarDetailResponseDto.fromEntity(usedCarEntity, carOptionsEntity, carImagesUrl);
    }

    public List<CarListResponseDto> carList(){
        List<UsedCarEntity> usedCarEntity = usedCarRepository.findAll();
        if(usedCarEntity.isEmpty()){
            return null;
        }

        List<CarListResponseDto> carListDto = new ArrayList<>();

        for(UsedCarEntity entity : usedCarEntity){
            String imageUrl = getImageUrl(entity.getId());

            CarListResponseDto carDto = CarListResponseDto.fromEntity(entity, imageUrl);
            carListDto.add(carDto);
        }

        return carListDto;
    }

    private String getImageUrl(Long usedCarId) {
        List<CarImageEntity> carImageEntities = carImageRepository.findByUsedCarId(usedCarId);
        if (carImageEntities.isEmpty()) {
            return null;
        }
        return carImageEntities.get(0).getImageUrl();
    }

    private List<String> getImageUrls(Long usedCarId) {
        List<CarImageEntity> carImageEntities = carImageRepository.findByUsedCarId(usedCarId);
        if (carImageEntities.isEmpty()) {
            return Collections.emptyList();
        }

        return carImageEntities.stream()
                .map(CarImageEntity::getImageUrl)
                .collect(Collectors.toList());
    }
}
