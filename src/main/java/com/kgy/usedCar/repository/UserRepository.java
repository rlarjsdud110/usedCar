package com.kgy.usedCar.repository;

import com.kgy.usedCar.model.UserEntity;
import com.kgy.usedCar.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserId(String userId);
    Optional<UserEntity> findByEmail(String userId);
    long countByRole(UserRole role);
}
