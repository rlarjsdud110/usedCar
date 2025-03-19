package com.kgy.usedCar.service;

import com.kgy.usedCar.config.JwtTokenProvider;
import com.kgy.usedCar.dto.request.user.UserUpdateRequestDto;
import com.kgy.usedCar.dto.response.user.*;
import com.kgy.usedCar.dto.request.user.UserLoginRequest;
import com.kgy.usedCar.dto.request.user.UserSignupRequest;
import com.kgy.usedCar.exception.ErrorCode;
import com.kgy.usedCar.exception.UsedCarException;
import com.kgy.usedCar.model.*;
import com.kgy.usedCar.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final CartRepository cartRepository;
    private final UsedCarRepository usedCarRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;

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

    public UserLoginResponseDto login(UserLoginRequest request){
        UserDto userDto = userRepository.findByUserId(request.getUserId()).map(UserDto::fromEntity)
                .orElseThrow(() -> new UsedCarException(ErrorCode.USER_NOT_FOUND, request.getUserId()));

        if(!passwordEncoder.matches(request.getPassword(), userDto.getPassword())){
            throw new UsedCarException(ErrorCode.INVALID_PASSWORD, "");
        }

        return new UserLoginResponseDto(tokenProvider.generateToken(userDto), userDto.getRole());
    }

    public UserInfoResponseDto userInfo(String userId){
        UserEntity userEntity = findUserById(userId);
        return UserInfoResponseDto.fromEntity(userEntity);
    }

    public UserInfoResponseDto update(String userId, UserUpdateRequestDto dto){
        UserEntity userEntity = findUserById(userId);
        userEntity.update(dto);
        return UserInfoResponseDto.fromEntity(userEntity);
    }

    public List<CartResponseDto> getCart(String userId){
        UserEntity userEntity = findUserById(userId);

        List<CartEntity> cartEntity = cartRepository.findByUser_IdOrderByCreatedAtDesc(userEntity.getId());

        return cartEntity.stream()
                .map(cartEntityList -> new CartResponseDto(
                        cartEntityList.getUsedCar().getId(),
                        cartEntityList.getUsedCar().getModel()
                ))
                .collect(Collectors.toList());
    }

    public void addCart(Long carId, String userId){
        UserEntity userEntity = findUserById(userId);
        UsedCarEntity usedCarEntity = findUsedCarById(carId);

        cartRepository.save(CartEntity.of(userEntity, usedCarEntity));
    }

    public void deleteCart(Long carId, String userId){
        UserEntity userEntity = findUserById(userId);

        CartEntity cartEntity = cartRepository.findByUserIdAndUsedCarId(userEntity.getId() ,carId)
                        .orElseThrow(() -> new UsedCarException(ErrorCode.CAR_NOT_FOUND));

        cartRepository.deleteById(cartEntity.getId());
    }

    public void purchaseRequest(Long carId, String userId){
        UserEntity userEntity = findUserById(userId);
        UsedCarEntity usedCarEntity = findUsedCarById(carId);

        boolean existPurchase = purchaseRequestRepository.existsByUserIdAndUsedCarId(userEntity.getId(), usedCarEntity.getId());

        if (existPurchase) {
            throw new UsedCarException(ErrorCode.DUPLICATED_PURCHASE);
        }

        PurchaseRequestEntity purchaseEntity = PurchaseRequestEntity.of(userEntity, usedCarEntity);
        purchaseRequestRepository.save(purchaseEntity);
    }

    public List<PurchaseResponse> purchaseList(String userId){
        UserEntity userEntity = findUserById(userId);

        List<PurchaseRequestEntity> purchaseRequestEntityList = purchaseRequestRepository.findAllByUserIdOrderByCreatedAtDesc(userEntity.getId());

        List<PurchaseResponse> purchaseResponseList = new ArrayList<>();

        for(PurchaseRequestEntity entity : purchaseRequestEntityList){
            PurchaseResponse purchaseDto = PurchaseResponse.fromEntity(entity);
            purchaseResponseList.add(purchaseDto);
        }

        return purchaseResponseList;
    }

    public void purchaseDelete(Long purchaseId){
        PurchaseRequestEntity purchaseRequest = purchaseRequestRepository.findById(purchaseId)
                        .orElseThrow(() -> new UsedCarException(ErrorCode.CAR_NOT_FOUND));
        purchaseRequestRepository.deleteById(purchaseRequest.getId());
    }

    private UserEntity findUserById(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.USER_NOT_FOUND));
    }

    private UsedCarEntity findUsedCarById(Long carId) {
        return usedCarRepository.findById(carId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.CAR_NOT_FOUND));
    }

}
