package com.example.domain.auth.service;

import com.example.domain.auth.AuthorizeContextHolder;
import com.example.domain.auth.exception.AuthorizeException;
import com.example.domain.auth.model.Authorize;
import com.example.domain.auth.repository.AuthorizeRepository;
import com.example.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import static org.apache.commons.codec.digest.HmacAlgorithms.HMAC_MD5;

@Slf4j
@Service
@AllArgsConstructor
public class AuthorizeService {
    public static final String AES_KEY = "aes-key";
    private final AuthorizeRepository repository;

    public Authorize create(User user) {
        return repository.create(generateToken(user.getId()), user.getId());
    }

    public Authorize current() {
        Authorize authorize = AuthorizeContextHolder.getContext();
        if (authorize.getUserId() == null) {
            throw AuthorizeException.Unauthorized();
        }

        return authorize;
    }

    private String generateToken(String userId) {
        String token = userId + "." + RandomStringUtils.randomAlphabetic(5).toLowerCase();
        token = token + "." + new HmacUtils(HMAC_MD5, AES_KEY).hmacHex(token);
        return token;
    }
}
