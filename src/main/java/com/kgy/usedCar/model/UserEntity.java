package com.kgy.usedCar.model;

import com.kgy.usedCar.dto.request.user.UserSignupRequest;
import com.kgy.usedCar.dto.request.user.UserUpdateRequestDto;
import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_Id")
    private String userId;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    public static UserEntity of(UserSignupRequest request, String encodedPassword) {
        return UserEntity.builder()
                .userId(request.getUserId())
                .email(request.getEmail())
                .name(request.getName())
                .password(encodedPassword)
                .phone(request.getPhone())
                .role(UserRole.USER)
                .build();
    }

    public void update(UserUpdateRequestDto dto){
        this.setName(dto.getName());
        this.setEmail(dto.getEmail());
        this.setPhone(dto.getPhone());
    }
    public static UserEntity CreatedAdmin(String adminId, String adminEmail, String adminName, String adminPassword, String adminPhone, UserRole role){
        return UserEntity.builder()
                .userId(adminId)
                .email(adminEmail)
                .name(adminName)
                .password(adminPassword)
                .phone(adminPhone)
                .role(role)
                .build();
    }
}