package com.seahere.backend.redis.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Getter
@NoArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 1209600000)
public class Token{
    @Id
    private Long id;

    @Indexed
    private String email;

    @Indexed
    private String refreshToken;

    @Builder
    public Token(Long id, String email, String refreshToken){
        this.id = id;
        this.email = email;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
}