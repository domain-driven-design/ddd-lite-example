package com.example.domain.auth.service;

import com.example.domain.auth.AuthorizeContextHolder;
import com.example.domain.auth.exception.AuthorizeException;
import com.example.domain.auth.model.Authorize;
import com.example.domain.auth.repository.AuthorizeRepository;
import com.example.domain.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthorizeService {
    @Autowired
    private AuthorizeRepository repository;

    public Authorize create(User user, String password) {
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw AuthorizeException.invalidCredential();
        }

        Authorize authorize = Authorize.builder()
                .userId(user.getId())
                .role(user.getRole())
                .build();
        return repository.create(authorize);
    }

    public Authorize current() {
        Authorize authorize = AuthorizeContextHolder.getContext();
        if (authorize == null || authorize.getUserId() == null) {
            throw AuthorizeException.Unauthorized();
        }

        return authorize;
    }

    public void delete(String id) {
        repository.delete(id);
    }
}
