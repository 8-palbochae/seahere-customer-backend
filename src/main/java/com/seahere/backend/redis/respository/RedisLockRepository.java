package com.seahere.backend.redis.respository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public Boolean lock(Long key, String type){
        return redisTemplate
                .opsForValue()
                .setIfAbsent(generateKey(key,type), "lock", Duration.ofMillis(3_000));
    }

    public Boolean unLock(Long key, String type){
        return redisTemplate.delete(generateKey(key,type));
    }

    private String generateKey(Long key,String type) {
        return key.toString() + type;
    }
}
