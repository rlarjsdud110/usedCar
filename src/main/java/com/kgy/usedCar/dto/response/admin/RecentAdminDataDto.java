package com.kgy.usedCar.dto.response.admin;

import com.kgy.usedCar.model.ConsultEntity;
import com.kgy.usedCar.model.NoticeEntity;
import com.kgy.usedCar.model.UsedCarEntity;
import com.kgy.usedCar.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RecentAdminDataDto {
    private Long carId;
    private String carModel;
    private String carModelYear;
    private int carPrice;
    private LocalDateTime carCreatedAt;

    private Long noticeId;
    private String noticeTitle;
    private String noticeContent;
    private LocalDateTime noticeCreatedAt;

    private Long consultId;
    private String consultEmail;
    private String consultTitle;
    private String consultStatus;
    private LocalDateTime consultCreatedAt;

    private Long id;
    private String username;
    private String userEmail;
    private String userId;
    private LocalDateTime userCreatedAt;

    public static RecentAdminDataDto of(UsedCarEntity usedCarEntity, NoticeEntity noticeEntity,
                                       ConsultEntity consultEntity, UserEntity userEntity ) {
        return new RecentAdminDataDto(
                usedCarEntity.getId(),
                usedCarEntity.getModel(),
                String.valueOf(usedCarEntity.getModelYear()),
                usedCarEntity.getPrice(),
                usedCarEntity.getCreatedAt(),

                noticeEntity.getId(),
                noticeEntity.getTitle(),
                noticeEntity.getContent(),
                noticeEntity.getCreatedAt(),

                consultEntity.getId(),
                consultEntity.getEmail(),
                consultEntity.getTitle(),
                consultEntity.getStatusType(),
                consultEntity.getCreatedAt(),

                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getUserId(),
                userEntity.getCreatedAt()
        );
    }
}
