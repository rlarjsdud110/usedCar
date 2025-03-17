package com.kgy.usedCar.service;

import com.kgy.usedCar.dto.request.admin.AdminConsultRequestDto;
import com.kgy.usedCar.dto.response.consult.ConsultListResponseDto;
import com.kgy.usedCar.exception.ErrorCode;
import com.kgy.usedCar.exception.UsedCarException;
import com.kgy.usedCar.model.ConsultEntity;
import com.kgy.usedCar.repository.ConsultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final ConsultRepository consultRepository;

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

}
