package com.kgy.usedCar.service;

import com.kgy.usedCar.config.JwtTokenProvider;
import com.kgy.usedCar.dto.request.consult.ConsultRequestDto;
import com.kgy.usedCar.dto.request.user.UserUpdateRequestDto;
import com.kgy.usedCar.dto.response.user.CartResponseDto;
import com.kgy.usedCar.dto.response.user.ConsultResponseDto;
import com.kgy.usedCar.dto.response.user.UserDto;
import com.kgy.usedCar.dto.request.user.UserLoginRequest;
import com.kgy.usedCar.dto.request.user.UserSignupRequest;
import com.kgy.usedCar.dto.response.user.UserInfoResponseDto;
import com.kgy.usedCar.exception.ErrorCode;
import com.kgy.usedCar.exception.UsedCarException;
import com.kgy.usedCar.model.*;
import com.kgy.usedCar.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final ConsultRepository consultRepository;
    private final CartRepository cartRepository;
    private final UsedCarRepository usedCarRepository;
    private final ConsultImageRepository consultImageRepository;

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

        List<CartEntity> cartEntity = cartRepository.findByUser_Id(userEntity.getId());

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

    public void deleteCart(Long carId){
        CartEntity cartEntity = cartRepository.findByUsedCar_Id(carId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.CART_NOT_FOUND));

        cartRepository.deleteById(cartEntity.getId());
    }

    @Transactional
    public void consultRequest(String userId, ConsultRequestDto dto, MultipartFile[] multipartFile){
        try {
            UserEntity userEntity = findUserById(userId);
            ConsultEntity consultEntity = ConsultEntity.of(userEntity, dto);

            ConsultEntity savedConsultEntity = consultRepository.save(consultEntity);

            if (multipartFile != null && multipartFile.length > 0) {
                s3Service.uploadConsultImages(multipartFile, savedConsultEntity);
            }

        } catch (IOException e) {
            throw new UsedCarException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public List<ConsultResponseDto> consultList(String userId){
        UserEntity userEntity = findUserById(userId);

        List<ConsultEntity> consultEntity = consultRepository.findByUser_Id(userEntity.getId());

        return consultEntity.stream()
                .map(consultEntities -> new ConsultResponseDto(
                        consultEntities.getId(),
                        consultEntities.getTitle(),
                        consultEntities.getStatusType(),
                        consultEntities.getTaskType(),
                        consultEntities.getEmail(),
                        consultEntities.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void consultUpdate(Long consultId, ConsultRequestDto dto, MultipartFile[] multipartFile){
        try {
            ConsultEntity consultEntity = consultRepository.findById(consultId)
                    .orElseThrow(() -> new UsedCarException(ErrorCode.CONSULT_NOT_FOUND));

            consultEntity.setEmail(dto.getEmail());
            consultEntity.setTitle(dto.getTitle());
            consultEntity.setContent(dto.getContent());
            consultEntity.setStatusType(dto.getStatusType());

            if (multipartFile != null && multipartFile.length > 0) {
                s3Service.updateConsultImages(multipartFile, consultEntity);
            }
        } catch (IOException e) {
            throw new UsedCarException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Transactional
    public void consultDelete(Long consultId){
        ConsultEntity consultEntity = consultRepository.findById(consultId)
                        .orElseThrow(() -> new UsedCarException(ErrorCode.CONSULT_NOT_FOUND));

        if(!consultEntity.getImages().isEmpty()){
            s3Service.deleteConsultImages(consultEntity);
        }

        consultRepository.delete(consultEntity);
    }

    private UserEntity findUserById(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.USER_NOT_FOUND));
    }

    private UsedCarEntity findUsedCarById(Long carId) {
        return usedCarRepository.findById(carId)
                .orElseThrow(() -> new UsedCarException(ErrorCode.CAR_NOT_FOUND));
    }

    private List<String> getImageUrls(Long consultId) {
        List<ConsultImageEntity> consultImageEntities = consultImageRepository.findByConsultId(consultId);
        if (consultImageEntities.isEmpty()) {
            return Collections.emptyList();
        }

        return consultImageEntities.stream()
                .map(ConsultImageEntity::getImageUrl)
                .collect(Collectors.toList());
    }

}
