package com.example.domain.user.service;

import com.example.domain.user.exception.UserException;
import com.example.domain.user.model.Operator;
import com.example.domain.user.model.User;
import com.example.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public User get(Specification<User> specification) {
        return repository.findOne(specification).orElseThrow(UserException::notFound);
    }

    public Page<User> findAll(Specification<User> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

    public List<User> findAll(Specification<User> specification) {
        return repository.findAll(specification);
    }

    public User create(String name, String email, String password) {
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        User user = User.build(name, email, encodedPassword);

        Specification<User> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(User.Fields.email), email);
        if (repository.count(specification) > 0) {
            throw UserException.emailConflicted();
        }

        return repository.save(user);
    }

    public User update(String id, String name, Operator operator) {
        User user = _get(id);

        if (!id.equals(operator.getUserId())) {
            throw UserException.noPermissionUpdate();
        }

        user.setName(name);
        return repository.save(user);
    }

    public User resetPassword(String id, String password, Operator operator) {
        User user = _get(id);

        if (!id.equals(operator.getUserId())) {
            throw UserException.noPermissionUpdate();
        }

        user.setPassword(bCryptPasswordEncoder.encode(password));
        return repository.save(user);
    }

    public User updateStatus(String id, User.Status status, Operator operator) {
        if (!operator.getRole().equals(User.Role.ADMIN)) {
            throw UserException.noPermissionUpdate();
        }

        User user = _get(id);
        user.setStatus(status);

        return repository.save(user);
    }
}
