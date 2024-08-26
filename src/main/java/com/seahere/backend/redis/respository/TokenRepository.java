package com.seahere.backend.redis.respository;

import com.seahere.backend.redis.entity.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {
    Optional<Token> findByEmail(String email);
    Optional<Token> findByRefreshToken(String refreshToken);
}