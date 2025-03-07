package com.kgy.usedCar.service;

import com.kgy.usedCar.config.JwtTokenProvider;
import com.kgy.usedCar.dto.response.user.UserDto;
import com.kgy.usedCar.dto.request.user.UserLoginRequest;
import com.kgy.usedCar.dto.request.user.UserSignupRequest;
import com.kgy.usedCar.dto.response.user.UserInfoResponseDto;
import com.kgy.usedCar.exception.ErrorCode;
import com.kgy.usedCar.exception.UsedCarException;
import com.kgy.usedCar.model.UserEntity;
import com.kgy.usedCar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public void signup(UserSignupRequest request){
        userRepository.findByUserId(request.getUserId()).ifPresent(user -> {
            throw new UsedCarException(ErrorCode.DUPLICATED_USER_ID, String.format("%s이 중복된 아이디 입니다.", request.getUserId()));
        });

        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new UsedCarException(ErrorCode.DUPLICATED_USER_EMAIL, String.format("%s이 중복된 이메일 입니다.", request.getUserId()));
        });

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        userRepository.save(UserEntity.of(request, encodedPassword));
    }

    public String login(UserLoginRequest request){
        UserDto userDto = userRepository.findByUserId(request.getUserId()).map(UserDto::fromEntity)
                .orElseThrow(() -> new UsedCarException(ErrorCode.USER_NOT_FOUND, request.getUserId()));

        if(!passwordEncoder.matches(request.getPassword(), userDto.getPassword())){
            throw new UsedCarException(ErrorCode.INVALID_PASSWORD, "");
        }

        return tokenProvider.generateToken(userDto);
    }

    public UserInfoResponseDto userInfo(String userId){
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.USER_NOT_FOUND));

        return UserInfoResponseDto.fromEntity(userEntity);
    }

}
