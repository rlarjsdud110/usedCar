package com.kgy.usedCar.repository;

import com.kgy.usedCar.model.UsedCarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsedCarRepository extends JpaRepository<UsedCarEntity, Long> {
}
