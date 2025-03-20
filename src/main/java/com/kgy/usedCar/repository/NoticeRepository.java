package com.kgy.usedCar.repository;

import com.kgy.usedCar.model.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
    List<NoticeEntity> findTop3ByOrderByCreatedAtDesc();
}
