package com.seahere.backend.redis.respository;

import com.seahere.backend.redis.entity.FCMToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FCMTokenRepository extends CrudRepository<FCMToken, Long> {
    Optional<FCMToken> findById(Long userId);
    Optional<FCMToken> findByEmail(String email);
    Optional<FCMToken> findByRefreshToken(String refreshToken);
}