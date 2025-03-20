package com.kgy.usedCar.dto.response.user;

import com.kgy.usedCar.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String userId;
    private LocalDateTime createdAt;

    public static UserInfoResponseDto fromEntity(UserEntity userEntity){
        return new UserInfoResponseDto(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getPhone(),
                userEntity.getEmail(),
                userEntity.getUserId(),
                userEntity.getCreatedAt()
        );
    }
}
