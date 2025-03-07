package com.kgy.usedCar.dto.response.user;

import com.kgy.usedCar.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {
    private String name;
    private String phone;
    private String email;

    public static UserInfoResponseDto fromEntity(UserEntity userEntity){
        return new UserInfoResponseDto(
                userEntity.getName(),
                userEntity.getPhone(),
                userEntity.getEmail()
        );
    }
}
