package com.kgy.usedCar.service;

import com.kgy.usedCar.dto.response.notice.NoticeResponseDto;
import com.kgy.usedCar.exception.ErrorCode;
import com.kgy.usedCar.exception.UsedCarException;
import com.kgy.usedCar.model.NoticeEntity;
import com.kgy.usedCar.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public List<NoticeResponseDto> getAllNotice(){
        List<NoticeEntity> noticeEntity = noticeRepository.findAll();

        if (noticeEntity.isEmpty()){
            return null;
        }

        return noticeEntity.stream()
                .map(NoticeResponseDto::fromEntity)
                .toList();
    }

    public void createNotice(NoticeResponseDto dto){
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        noticeRepository.save(noticeEntity);
    }

    public void updateNotice(Long id, NoticeResponseDto dto){
        NoticeEntity noticeEntity = noticeRepository.findById(id)
                .orElseThrow(() -> new UsedCarException(ErrorCode.NOTICE_NOT_FOUND));

        noticeEntity.update(dto.getTitle(), dto.getContent());
    }

    public void deleteNotice(Long id){
        NoticeEntity noticeEntity = noticeRepository.findById(id)
                .orElseThrow(() -> new UsedCarException(ErrorCode.NOTICE_NOT_FOUND));

        noticeRepository.delete(noticeEntity);
    }
}
