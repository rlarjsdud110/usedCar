package com.kgy.usedCar.service;

import com.kgy.usedCar.dto.request.admin.AdminConsultRequestDto;
import com.kgy.usedCar.dto.request.car.CarRequestDto;
import com.kgy.usedCar.dto.response.admin.DashboardStatsDTO;
import com.kgy.usedCar.dto.response.admin.PurchaseListResponseDto;
import com.kgy.usedCar.dto.response.admin.RecentAdminDataDto;
import com.kgy.usedCar.dto.response.consult.ConsultListResponseDto;
import com.kgy.usedCar.dto.response.user.UserInfoResponseDto;
import com.kgy.usedCar.exception.ErrorCode;
import com.kgy.usedCar.exception.UsedCarException;
import com.kgy.usedCar.model.*;
import com.kgy.usedCar.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final ConsultRepository consultRepository;
    private final UsedCarRepository usedCarRepository;
    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final CarOptionRepository carOptionRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;

    private final S3Service s3Service;


    public List<ConsultListResponseDto> consultList(){
        List<ConsultEntity> consultEntity = consultRepository.findAll(Sort.by(Sort.Order.desc("createdAt")));

        return consultEntity.stream()
                .map(consultEntities -> new ConsultListResponseDto(
                        consultEntities.getId(),
                        consultEntities.getTitle(),
                        consultEntities.getStatusType(),
                        consultEntities.getTaskType(),
                        consultEntities.getEmail(),
                        consultEntities.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void consultAnswer(Long consultId, AdminConsultRequestDto dto){
        ConsultEntity consultEntity = consultRepository.findById(consultId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.CONSULT_NOT_FOUND));

        consultEntity.setAnswer(dto.getAnswer());
        consultEntity.setTaskType(dto.getTaskType());

        consultRepository.save(consultEntity);
    }

    public DashboardStatsDTO dashboard(){
        long vehicleCount = usedCarRepository.count();
        long totalUsersCount = userRepository.count();
        long unresolvedConsultCount = consultRepository.countByTaskType("접수완료");
        long totalNoticesCount = noticeRepository.count();

        return DashboardStatsDTO.of(vehicleCount, totalUsersCount, unresolvedConsultCount, totalNoticesCount);
    }

    public List<RecentAdminDataDto> recentAdminData(){
        List<UserEntity> userEntity = userRepository.findTop3ByOrderByCreatedAtDesc();
        List<NoticeEntity> noticeEntity = noticeRepository.findTop3ByOrderByCreatedAtDesc();
        List<UsedCarEntity> usedCarEntity = usedCarRepository.findTop3ByOrderByCreatedAtDesc();
        List<ConsultEntity> consultEntity = consultRepository.findTop3ByOrderByCreatedAtDesc();

        List<RecentAdminDataDto> recentAdminData = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            UsedCarEntity usedCar = i < usedCarEntity.size() ? usedCarEntity.get(i) : null;
            NoticeEntity notice = i < noticeEntity.size() ? noticeEntity.get(i) : null;
            ConsultEntity consult = i < consultEntity.size() ? consultEntity.get(i) : null;
            UserEntity user = i < userEntity.size() ? userEntity.get(i) : null;

            RecentAdminDataDto dto = RecentAdminDataDto.of(
                    usedCar != null ? usedCar : new UsedCarEntity(),
                    notice != null ? notice : new NoticeEntity(),
                    consult != null ? consult : new ConsultEntity(),
                    user != null ? user : new UserEntity()
            );

            recentAdminData.add(dto);
        }

        return recentAdminData;
    }

    @Transactional
    public void deleteCar(Long carId){
        UsedCarEntity usedCarEntity = usedCarRepository.findById(carId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.CAR_NOT_FOUND));

        s3Service.deleteCarImages(carId);
        usedCarRepository.delete(usedCarEntity);
    }

    @Transactional
    public void createCar(CarRequestDto dto, MultipartFile[] multipartFiles){
        try {
            UsedCarEntity usedCarEntity = UsedCarEntity.fromDto(dto.getUsedCarDto());
            UsedCarEntity usedCar = usedCarRepository.save(usedCarEntity);

            CarOptionEntity carOptionEntity = CarOptionEntity.fromDto(dto.getCarOptionsDto(), usedCar);
            carOptionRepository.save(carOptionEntity);

            if (multipartFiles != null && multipartFiles.length > 0) {
                s3Service.uploadCarImages(multipartFiles, usedCar.getId(), dto.getImageTypes());
            }

        } catch (IOException e) {
            throw new UsedCarException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Transactional
    public void updatedCar(Long carId, CarRequestDto dto, MultipartFile[] multipartFiles){
        try {
            UsedCarEntity usedCarEntity = usedCarRepository.findById(carId)
                    .orElseThrow(() -> new UsedCarException(ErrorCode.CAR_NOT_FOUND));

            CarOptionEntity carOptionEntity = carOptionRepository.findByUsedCar_Id(usedCarEntity.getId())
                    .orElseThrow(() -> new UsedCarException(ErrorCode.CAR_NOT_FOUND));

            UsedCarEntity savedUsedCar = usedCarEntity.update(dto.getUsedCarDto());
            usedCarRepository.save(savedUsedCar);

            CarOptionEntity savedCarOptions = carOptionEntity.update(dto.getCarOptionsDto());
            carOptionRepository.save(savedCarOptions);

            if (multipartFiles != null && multipartFiles.length > 0) {
                s3Service.updateCarImages(multipartFiles, savedUsedCar.getId(), dto.getImageTypes());
            }

        } catch (IOException e) {
            throw new UsedCarException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public List<PurchaseListResponseDto> purchaseList(){
        List<PurchaseRequestEntity> purchaseRequest = purchaseRequestRepository.findAll();

        List<PurchaseListResponseDto> responseDto = new ArrayList<>();
        if(!purchaseRequest.isEmpty()){
            for (PurchaseRequestEntity entity : purchaseRequest){

                PurchaseListResponseDto value = PurchaseListResponseDto.of(entity.getUser().getName(), entity.getUser().getPhone(),
                        entity.getUsedCar().getId(), entity.getId(), entity.getCreatedAt());

                responseDto.add(value);
            }
        }
        return responseDto;
    }

    public void purchaseDelete(Long purchaseId){
        PurchaseRequestEntity purchaseRequest = purchaseRequestRepository.findById(purchaseId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.PURCHASE_NOT_FOUND));

        purchaseRequestRepository.deleteById(purchaseRequest.getId());
    }

    public Page<UserInfoResponseDto> userInfoList(String search, Pageable pageable){
        Page<UserEntity> userEntityPage;

        if (search == null || search.trim().isEmpty()) {
            userEntityPage = userRepository.findByRole(UserRole.USER, pageable);
        } else {
            userEntityPage = userRepository.findByRoleAndEmailContainingIgnoreCaseOrRoleAndNameContainingIgnoreCase(UserRole.USER, search, UserRole.USER, search, pageable);
        }

        if (userEntityPage.isEmpty()) {
            return Page.empty();
        }

        List<UserInfoResponseDto> userInfoResponseDto = userEntityPage.stream()
                .map(UserInfoResponseDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(userInfoResponseDto, pageable, userEntityPage.getTotalElements());
    }

}
