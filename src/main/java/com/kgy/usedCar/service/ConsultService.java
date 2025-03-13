package com.kgy.usedCar.service;

import com.kgy.usedCar.dto.request.consult.ConsultRequestDto;
import com.kgy.usedCar.dto.response.consult.ConsultListResponseDto;
import com.kgy.usedCar.dto.response.consult.ConsultResponseDto;
import com.kgy.usedCar.exception.ErrorCode;
import com.kgy.usedCar.exception.UsedCarException;
import com.kgy.usedCar.model.ConsultEntity;
import com.kgy.usedCar.model.ConsultImageEntity;
import com.kgy.usedCar.model.UserEntity;
import com.kgy.usedCar.repository.ConsultImageRepository;
import com.kgy.usedCar.repository.ConsultRepository;
import com.kgy.usedCar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultService {

    private final S3Service s3Service;
    private final ConsultRepository consultRepository;
    private final UserRepository userRepository;
    private final ConsultImageRepository consultImageRepository;


    @Transactional
    public void consultRequest(String userId, ConsultRequestDto dto, MultipartFile[] multipartFile){
        try {
            UserEntity userEntity = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new UsedCarException(ErrorCode.USER_NOT_FOUND));

            ConsultEntity consultEntity = ConsultEntity.of(userEntity, dto);

            ConsultEntity savedConsultEntity = consultRepository.save(consultEntity);

            if (multipartFile != null && multipartFile.length > 0) {
                s3Service.uploadConsultImages(multipartFile, savedConsultEntity);
            }

        } catch (IOException e) {
            throw new UsedCarException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public ConsultResponseDto consult(Long consultId){
        ConsultEntity consultEntity = consultRepository.findById(consultId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.CONSULT_NOT_FOUND));

        List<String> urls = getImageUrls(consultEntity.getId());

        return ConsultResponseDto.fromEntity(consultEntity, urls);
    }

    public List<ConsultListResponseDto> consultList(String userId){
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.USER_NOT_FOUND));

        List<ConsultEntity> consultEntity = consultRepository.findByUser_Id(userEntity.getId());

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
    public void consultUpdate(Long consultId, ConsultRequestDto dto, MultipartFile[] multipartFile){
        try {
            ConsultEntity consultEntity = consultRepository.findById(consultId)
                    .orElseThrow(() -> new UsedCarException(ErrorCode.CONSULT_NOT_FOUND));

            consultEntity.setEmail(dto.getEmail());
            consultEntity.setTitle(dto.getTitle());
            consultEntity.setContent(dto.getContent());
            consultEntity.setStatusType(dto.getStatusType());

            if (multipartFile != null && multipartFile.length > 0) {
                s3Service.updateConsultImages(multipartFile, consultEntity);
            }
        } catch (IOException e) {
            throw new UsedCarException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Transactional
    public void consultDelete(Long consultId){
        ConsultEntity consultEntity = consultRepository.findById(consultId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.CONSULT_NOT_FOUND));

        if(!consultEntity.getImages().isEmpty()){
            s3Service.deleteConsultImages(consultEntity);
        }

        consultRepository.delete(consultEntity);
    }

    private List<String> getImageUrls(Long consultId) {
        List<ConsultImageEntity> consultImageEntities = consultImageRepository.findByConsultId(consultId);
        if (consultImageEntities.isEmpty()) {
            return Collections.emptyList();
        }

        return consultImageEntities.stream()
                .map(ConsultImageEntity::getImageUrl)
                .collect(Collectors.toList());
    }
}
