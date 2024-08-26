package com.seahere.backend.user.repository;

import com.seahere.backend.common.entity.SocialType;
import com.seahere.backend.user.domain.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> , UserRepositoryCustom {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByRefreshToken(String refreshToken);
    Optional<UserEntity> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
    Boolean existsByEmail(String email);
}
