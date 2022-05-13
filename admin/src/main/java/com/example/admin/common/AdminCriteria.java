package com.example.admin.common;

import com.example.domain.user.model.User;
import org.springframework.data.jpa.domain.Specification;

public class AdminCriteria {
    public static Specification<User> ofName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get(User.Fields.name), name),
                criteriaBuilder.equal(root.get(User.Fields.role), User.Role.ADMIN)
        );
    }
}
