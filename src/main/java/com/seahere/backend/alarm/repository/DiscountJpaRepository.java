package com.seahere.backend.alarm.repository;

import com.seahere.backend.alarm.entity.DiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiscountJpaRepository extends JpaRepository<DiscountEntity, Long> {

    @Query("SELECT d FROM DiscountEntity d LEFT JOIN FETCH d.inventory i LEFT JOIN FETCH i.inventoryDetail WHERE d.endDate < :endDate")
    List<DiscountEntity> findAllByEndDateBefore(@Param("endDate") LocalDate endDate);
}
