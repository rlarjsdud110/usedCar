package com.kgy.usedCar.service;

import com.kgy.usedCar.dto.request.UserSignupRequest;
import com.kgy.usedCar.exception.ErrorCode;
import com.kgy.usedCar.exception.UsedCarException;
import com.kgy.usedCar.model.UserEntity;
import com.kgy.usedCar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void signup(UserSignupRequest request){
        userRepository.findByUserId(request.getUserId()).ifPresent(user -> {
            throw new UsedCarException(ErrorCode.DUPLICATED_USER_ID, String.format("%s이 중복된 아이디 입니다.", request.getUserId()));
        });

        userRepository.save(UserEntity.of(request));

        //TODO jwt token return
    }
}
