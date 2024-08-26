package com.seahere.backend.follow.repository;

import com.seahere.backend.follow.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowJpaRepository extends JpaRepository<FollowEntity, Long> {
    Optional<FollowEntity> findByUserIdAndCompanyId(Long userId, Long companyId);
}
