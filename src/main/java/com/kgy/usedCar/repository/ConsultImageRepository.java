package com.kgy.usedCar.repository;

import com.kgy.usedCar.model.ConsultImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultImageRepository extends JpaRepository<ConsultImageEntity, Long> {
    List<ConsultImageEntity> findByConsultId(Long consultId);

}
