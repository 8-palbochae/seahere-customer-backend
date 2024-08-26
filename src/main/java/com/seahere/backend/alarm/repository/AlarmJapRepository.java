package com.seahere.backend.alarm.repository;

import com.seahere.backend.alarm.entity.AlarmTokenEntity;
import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlarmJapRepository extends JpaRepository<AlarmTokenEntity, Long> {

    Optional<AlarmTokenEntity> findByUser(UserEntity user);
}
