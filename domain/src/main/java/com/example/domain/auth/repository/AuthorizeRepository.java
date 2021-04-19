package com.example.domain.auth.repository;

import com.example.domain.auth.model.Authorize;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@AllArgsConstructor
public class AuthorizeRepository {
    public static final long DEFAULT_EXPIRE = 60 * 10;
    private final RedisTemplate<String, String> redisTemplate;

    public Authorize create(String id, String userId) {
        redisTemplate.boundValueOps(generateKey(id))
                .set(userId, DEFAULT_EXPIRE, TimeUnit.SECONDS);
        return Authorize.builder()
                .id(id)
                .userId(userId)
                .expire(DEFAULT_EXPIRE)
                .build();
    }

    public Authorize get(String id) {
        String key = generateKey(id);
        String userId = redisTemplate.boundValueOps(generateKey(id)).get();
        Long expire = redisTemplate.getExpire(key);
        return Authorize.builder()
                .id(id)
                .userId(userId)
                .expire(expire)
                .build();
    }

    private String generateKey(String id) {
        return "token:" + id;
    }
}
