package com.kgy.usedCar.repository;

import com.kgy.usedCar.model.ConsultEntity;
import com.kgy.usedCar.model.UsedCarEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsedCarRepository extends JpaRepository<UsedCarEntity, Long> {
    List<UsedCarEntity> findTop5ByOrderByViewCountDesc();
    List<UsedCarEntity> findTop3ByOrderByViewCountDesc();
    List<UsedCarEntity> findTop5ByOrderByCreatedAtDesc();
    List<UsedCarEntity> findByIsHotDealTrue();
    Page<UsedCarEntity> findByModelContaining(String searchName, Pageable pageable);
    List<UsedCarEntity> findTop3ByOrderByCreatedAtDesc();

}
