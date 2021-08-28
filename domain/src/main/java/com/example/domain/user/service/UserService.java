package com.example.domain.user.service;

import com.example.domain.user.exception.UserException;
import com.example.domain.user.model.User;
import com.example.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

// TODO user status check
@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User get(String id) {
        return _get(id);
    }

    private User _get(String id) {
        return repository.findById(id).orElseThrow(UserException::notFound);
    }

    public User get(Example<User> example) {
        return repository.findOne(example).orElseThrow(UserException::notFound);
    }

    public Page<User> findAll(Specification<User> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }

    // TODO 保留创建者信息，区分创建来源
    public User create(String name, String email, String password) {
        User user = User.builder()
                .name(name)
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .role(User.UserRole.USER)
                .status(User.Status.NORMAL)
                .build();
        validateConflicted(user);
        return repository.save(user);
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

        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setUpdatedAt(Instant.now());
        return repository.save(user);
    }

    // TODO 是否将冻结/解冻分开处理？现阶段只是对status进行更改，并且对status的更改权限检查是一致的
    public User updateStatus(String id, User.Status status, User operator) {
        if (!operator.getRole().equals(User.UserRole.ADMIN)) {
            // TODO 权限异常区分粒度？status，message
            throw UserException.noPermissionUpdate();
        }

        User user = _get(id);
        user.setStatus(status);
        user.setUpdatedAt(Instant.now());

        return repository.save(user);
    }

    // TODO 是否在domain service检查唯一性？
    private void validateConflicted(User user) {
        if (repository.exists(Example.of(User.builder().email(user.getEmail()).build()))) {
            throw UserException.emailConflicted();
        }
    }
}
