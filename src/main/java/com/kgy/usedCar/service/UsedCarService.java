package com.kgy.usedCar.service;

import com.kgy.usedCar.dto.request.car.CarRequestDto;
import com.kgy.usedCar.dto.response.car.*;
import com.kgy.usedCar.exception.ErrorCode;
import com.kgy.usedCar.exception.UsedCarException;
import com.kgy.usedCar.model.*;
import com.kgy.usedCar.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final CartRepository cartRepository;

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

            s3Service.uploadCarImages(multipartFiles, usedCar.getId(), dto.getImageTypes());

        } catch (IOException e) {
            throw new UsedCarException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Transactional
    public void delete(Long carId){
        UsedCarEntity usedCarEntity = usedCarRepository.findById(carId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.CAR_NOT_FOUND));

       s3Service.deleteCarImages(carId);
       usedCarRepository.delete(usedCarEntity);

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

    public Page<SearchResponseDto> searchCars(Pageable pageable, String searchName){
        Page<UsedCarEntity> usedCarEntity = usedCarRepository.findByModelContaining(searchName, pageable);

        if (usedCarEntity.isEmpty()) {
            return Page.empty();
        }
        List<SearchResponseDto> searchResponseDto = new ArrayList<>();

        for (UsedCarEntity entity : usedCarEntity){
            String imageUrl = getImageUrl(entity.getId());
            SearchResponseDto dto = SearchResponseDto.fromEntity(entity, imageUrl);
            searchResponseDto.add(dto);
        }

        return new PageImpl<>(searchResponseDto, pageable, usedCarEntity.getTotalElements());
    }

    public CarDetailResponseDto carDetail(Long carId){
        UsedCarEntity usedCarEntity = usedCarRepository.findById(carId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.CAR_NOT_FOUND));

        CarOptionEntity carOptionsEntity = carOptionRepository.findByUsedCar_Id(carId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.CAR_NOT_FOUND));

        usedCarEntity.setViewCount(usedCarEntity.getViewCount() + 1);
        usedCarRepository.save(usedCarEntity);

        List<String> carImagesUrl = getImageUrls(carId);

        boolean isCart = cartRepository.existsByUsedCar_Id(carId);

        return CarDetailResponseDto.fromEntity(usedCarEntity, carOptionsEntity, carImagesUrl, isCart);
    }

    public List<RecommendCarDto> recommendCar(){
        List<UsedCarEntity> usedCarEntityList = usedCarRepository.findTop3ByOrderByViewCountDesc();
        if(usedCarEntityList.isEmpty()){
            return List.of();
        }

        List<RecommendCarDto> recommendCarDtoList = new ArrayList<>();
        for (UsedCarEntity entity : usedCarEntityList){
            String imageUrl = getImageUrl(entity.getId());
            RecommendCarDto dto = RecommendCarDto.fromEntity(entity, imageUrl);

            recommendCarDtoList.add(dto);
        }
        return recommendCarDtoList;
    }

    public Page<CarListResponseDto> carList(Pageable pageable){
        Page<UsedCarEntity> usedCarEntity = usedCarRepository.findAll(pageable);
        if (usedCarEntity.isEmpty()) {
            return Page.empty();
        }

        List<CarListResponseDto> carListResponseDto = new ArrayList<>();
        for (UsedCarEntity entity : usedCarEntity) {
            String imageUrl = getImageUrl(entity.getId());
            CarListResponseDto dto = CarListResponseDto.fromEntity(entity, imageUrl);
            carListResponseDto.add(dto);
        }

        return new PageImpl<>(carListResponseDto, pageable, usedCarEntity.getTotalElements());
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
