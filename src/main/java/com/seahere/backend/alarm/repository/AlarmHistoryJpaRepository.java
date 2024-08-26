package com.seahere.backend.alarm.repository;

import com.seahere.backend.alarm.entity.AlarmHistoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmHistoryJpaRepository extends JpaRepository<AlarmHistoryEntity, Long> {

    Slice<AlarmHistoryEntity> findByUserId(Long userId, Pageable pageable);
}
