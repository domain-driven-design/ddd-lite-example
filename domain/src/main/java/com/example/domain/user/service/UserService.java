package com.example.domain.user.service;

import com.example.domain.user.exception.UserException;
import com.example.domain.user.model.User;
import com.example.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    public User get(Example<User> example) {
        return repository.findOne(example).orElseThrow(UserException::notFound);
    }

    public Page<User> findAll(Specification<User> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }

    public User create(String name, String email, String password) {
        User user = User.builder()
                .name(name)
                .email(email)
                .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .role(User.UserRole.USER)
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

        user.setPassword(password);
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
