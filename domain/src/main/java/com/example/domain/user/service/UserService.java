package com.example.domain.user.service;

import com.example.domain.user.exception.UserException;
import com.example.domain.user.model.User;
import com.example.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User getById(String id) {
        return repository.findById(id).orElseThrow(UserException::notFound);
    }

    public User create(String name, String email, String password) {
        User user = User.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
        validateConflicted(user);
        return repository.save(user);
    }

    private void validateConflicted(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw UserException.emailConflicted();
        }
    }
}
