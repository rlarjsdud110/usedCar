package com.kgy.usedCar.repository;

import com.kgy.usedCar.model.CarImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarImageRepository extends JpaRepository<CarImageEntity, Long> {
    List<CarImageEntity> findByUsedCarId(Long carId);
}