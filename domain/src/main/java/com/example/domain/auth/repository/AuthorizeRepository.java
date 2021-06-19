package com.example.domain.auth.repository;

import com.example.domain.auth.model.Authorize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class AuthorizeRepository {
    public static final long DEFAULT_EXPIRE = 24 * 60 * 60;
    @Autowired
    private RedisTemplate<String, Authorize> redisTemplate;

    public Authorize create(Authorize authorize) {
        authorize.setId(UUID.randomUUID().toString());
        authorize.setExpire(DEFAULT_EXPIRE);
        redisTemplate.boundValueOps(generateKey(authorize.getId()))
                .set(authorize, DEFAULT_EXPIRE, TimeUnit.SECONDS);
        return  authorize;
    }

    public Authorize get(String id) {
        String key = generateKey(id);
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String id) {
        redisTemplate.delete(generateKey(id));
    }

    private String generateKey(String id) {
        return "token:" + id;
    }
}
