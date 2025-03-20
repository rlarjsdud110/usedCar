package com.kgy.usedCar.config;

import com.kgy.usedCar.model.UserEntity;
import com.kgy.usedCar.model.UserRole;
import com.kgy.usedCar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class AdminAccountInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.adminId:not found!}")
    String adminId;
    @Value("${admin.password:not found!}")
    String adminPassword;
    @Value("${admin.adminEmail:not found!}")
    String adminEmail;
    @Value("${admin.adminPhone:not found!}")
    String adminPhone;
    @Value("${admin.adminName:not found!}")
    String adminName;

    @EventListener(ApplicationReadyEvent.class)
    public void createAdminAccountIfNotExists() {
        if (userRepository.countByRole(UserRole.ADMIN) < 1) {
            String encodedPassword = passwordEncoder.encode(adminPassword);

            UserEntity admin = UserEntity.CreatedAdmin(adminId, adminEmail, adminName, encodedPassword, adminPhone, UserRole.ADMIN);

            userRepository.save(admin);
            log.info("관리자 계정이 생성되었습니다. (ID: {}, PW: hidden)", adminId);
        } else {
            log.info("관리자 계정이 이미 존재합니다.");
        }
    }
}