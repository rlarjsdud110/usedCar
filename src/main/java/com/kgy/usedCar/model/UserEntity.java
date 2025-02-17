package com.kgy.usedCar.model;

import com.kgy.usedCar.dto.request.UserSignupRequest;
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

    public static UserEntity of(UserSignupRequest request){

        return UserEntity.builder()
                .userId(request.getUserId())
                .email(request.getEmail())
                .name(request.getName())
                .password(request.getPassword())
                .phone(request.getPhone())
                .role(UserRole.USER)
                .build();
    }
}
