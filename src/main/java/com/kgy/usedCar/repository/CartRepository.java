package com.kgy.usedCar.repository;

import com.kgy.usedCar.model.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    List<CartEntity> findByUser_IdOrderByCreatedAtDesc(Long userId);
    Optional<CartEntity> findByUsedCar_Id(Long carId);

    boolean existsByUsedCar_Id(Long carId);
}
