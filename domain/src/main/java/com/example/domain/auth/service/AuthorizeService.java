package com.example.domain.auth.service;

import com.example.domain.auth.model.Authorize;
import com.example.domain.auth.repository.AuthorizeRepository;
import com.example.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import static org.apache.commons.codec.digest.HmacAlgorithms.HMAC_MD5;

@Service
@AllArgsConstructor
public class AuthorizeService {
    public static final String AES_KEY = "aes-key";
    private final AuthorizeRepository repository;

    public Authorize create(User user) {
        return repository.create(generateToken(user.getId()), user.getId());
    }

    public Authorize getById(String id) {
        return repository.get(id);
    }

    private String generateToken(String userId) {
        String token = userId + "." + RandomStringUtils.randomAlphabetic(5).toLowerCase();
        token = token + "." + new HmacUtils(HMAC_MD5, AES_KEY).hmacHex(token);
        return token;
    }
}
