package com.seahere.backend.alarm.repository;

import com.seahere.backend.alarm.entity.AlarmCustomerLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmCustomerLogJpaRepository extends JpaRepository<AlarmCustomerLogEntity, Long> {
}
