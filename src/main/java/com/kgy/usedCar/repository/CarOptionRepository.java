package com.kgy.usedCar.repository;

import com.kgy.usedCar.model.CarOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarOptionRepository extends JpaRepository<CarOptionEntity, Long> {
    Optional<CarOptionEntity> findByUsedCar_Id(Long id);
}
