package com.kgy.usedCar.repository;

import com.kgy.usedCar.model.ConsultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultRepository extends JpaRepository<ConsultEntity, Long> {
    List<ConsultEntity> findByUser_Id(Long userId);

}
