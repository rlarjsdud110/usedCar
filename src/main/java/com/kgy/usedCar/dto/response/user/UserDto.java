package com.kgy.usedCar.dto.response.user;

import com.kgy.usedCar.model.UserEntity;
import com.kgy.usedCar.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {

    private String userId;

    private String password;

    private String email;

    private String name;

    private String phone;

    private UserRole role;

    public static UserDto fromEntity(UserEntity entity) {
        return new UserDto(
                entity.getUserId(),
                entity.getPassword(),
                entity.getEmail(),
                entity.getName(),
                entity.getPhone(),
                entity.getRole()
        );
    }
}
