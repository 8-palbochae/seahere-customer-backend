package com.seahere.backend.adjust.repository;

import com.seahere.backend.adjust.entity.AdjustEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdjustJpaRepository extends JpaRepository<AdjustEntity, Long> {
}
