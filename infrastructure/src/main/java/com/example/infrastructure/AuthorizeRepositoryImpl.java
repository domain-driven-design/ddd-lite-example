package com.example.infrastructure;

import com.example.domain.iam.auth.model.Authorize;
import com.example.domain.iam.auth.repository.AuthorizeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class AuthorizeRepositoryImpl implements AuthorizeRepository {
    public static final long DEFAULT_EXPIRE = 24 * 60 * 60;
    @Autowired
    private RedisTemplate<String, Authorize> redisTemplate;

    @Override
    public Authorize create(Authorize authorize) {
        authorize.setExpire(DEFAULT_EXPIRE);
        redisTemplate.boundValueOps(generateKey(authorize.getId()))
                .set(authorize, DEFAULT_EXPIRE, TimeUnit.SECONDS);
        return authorize;
    }

    @Override
    public Authorize get(String id) {
        String key = generateKey(id);
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String id) {
        redisTemplate.delete(generateKey(id));
    }

    private String generateKey(String id) {
        return "token:" + id;
    }
}
