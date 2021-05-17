package com.example.domain.user.service;

import com.example.domain.user.exception.UserException;
import com.example.domain.user.model.User;
import com.example.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private UserRepository repository;

    public User get(String id) {
        return _get(id);
    }

    private User _get(String id) {
        return repository.findById(id).orElseThrow(UserException::notFound);
    }

    // TODO 编码password
    public User create(String name, String email, String password) {
        User user = User.builder()
                .name(name)
                .email(email)
                .password(password)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .role(User.UserRole.USER)
                .build();
        validateConflicted(user);
        return repository.save(user);
    }

    public User createAdmin(String name, String password, String operatorId) {
        // TODO 检查operator是admin吗？role检查在外层已被路由拦截过
        User admin = User.builder()
                .name(name)
                .password(password)
                .role(User.UserRole.ADMIN)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
       return repository.save(admin);
    }

    public User update(String id, String name, String operatorId) {
        User user = _get(id);

        if (!id.equals(operatorId)) {
            throw UserException.noPermissionUpdate();
        }

        user.setName(name);
        user.setUpdatedAt(Instant.now());
        return repository.save(user);
    }

    public User resetPassword(String id, String password, String operatorId) {
        User user = _get(id);

        if (!id.equals(operatorId)) {
            throw UserException.noPermissionUpdate();
        }

        user.setPassword(password);
        user.setUpdatedAt(Instant.now());
        return repository.save(user);
    }


    // TODO 是否在domain service检查唯一性？
    private void validateConflicted(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw UserException.emailConflicted();
        }
    }
}
