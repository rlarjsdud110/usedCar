package com.kgy.usedCar.service;

import com.kgy.usedCar.config.JwtTokenProvider;
import com.kgy.usedCar.dto.request.user.UserUpdateRequestDto;
import com.kgy.usedCar.dto.response.user.CartResponseDto;
import com.kgy.usedCar.dto.response.user.ConsultResponseDto;
import com.kgy.usedCar.dto.response.user.UserDto;
import com.kgy.usedCar.dto.request.user.UserLoginRequest;
import com.kgy.usedCar.dto.request.user.UserSignupRequest;
import com.kgy.usedCar.dto.response.user.UserInfoResponseDto;
import com.kgy.usedCar.exception.ErrorCode;
import com.kgy.usedCar.exception.UsedCarException;
import com.kgy.usedCar.model.CartEntity;
import com.kgy.usedCar.model.ConsultEntity;
import com.kgy.usedCar.model.UsedCarEntity;
import com.kgy.usedCar.model.UserEntity;
import com.kgy.usedCar.repository.CartRepository;
import com.kgy.usedCar.repository.ConsultRepository;
import com.kgy.usedCar.repository.UsedCarRepository;
import com.kgy.usedCar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final ConsultRepository consultRepository;
    private final CartRepository cartRepository;
    private final UsedCarRepository usedCarRepository;

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

    @Transactional
    public UserInfoResponseDto update(String userId, UserUpdateRequestDto dto){
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.USER_NOT_FOUND));

        userEntity.update(dto);

        return UserInfoResponseDto.fromEntity(userEntity);
    }

    public List<ConsultResponseDto> consultList(String userId){
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.USER_NOT_FOUND));

        List<ConsultEntity> consultEntity = consultRepository.findByUser_Id(userEntity.getId());

        return consultEntity.stream()
                .map(consultEntities -> new ConsultResponseDto(
                        consultEntities.getId(),
                        consultEntities.getTitle(),
                        consultEntities.getStatusType(),
                        consultEntities.getTaskType(),
                        consultEntities.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public List<CartResponseDto> getCart(String userId){
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.USER_NOT_FOUND));

        List<CartEntity> cartEntity = cartRepository.findByUser_Id(userEntity.getId());

        return cartEntity.stream()
                .map(cartEntityList -> new CartResponseDto(
                        cartEntityList.getUsedCar().getId(),
                        cartEntityList.getUsedCar().getModel()
                ))
                .collect(Collectors.toList());
    }

    public void addCart(Long carId, String userId){
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.USER_NOT_FOUND));

        UsedCarEntity usedCarEntity = usedCarRepository.findById(carId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.CAR_NOT_FOUND));

        CartEntity cartEntity = CartEntity.builder()
                .user(userEntity)
                .usedCar(usedCarEntity)
                .build();

        cartRepository.save(cartEntity);
    }

    @Transactional
    public void deleteCart(Long carId){
        CartEntity cartEntity = cartRepository.findByUsedCar_Id(carId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.CART_NOT_FOUND));

        cartRepository.deleteById(cartEntity.getId());
    }
}
