package com.seahere.backend.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "FCMToken", timeToLive = 1209600000)
public class FCMToken {
    @Id
    private Long id;

    @Indexed
    private String email;

    @Indexed
    private String FCMToken;

    public void updateFCMToken(String refreshToken){
        this.FCMToken = refreshToken;
    }
}
