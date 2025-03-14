package com.kgy.usedCar.repository;

import com.kgy.usedCar.model.PurchaseRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequestEntity, Long> {
    List<PurchaseRequestEntity> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    boolean existsByUserIdAndUsedCarId(Long userId, Long carId);
}
