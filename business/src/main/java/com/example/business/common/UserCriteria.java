package com.example.business.common;

import com.example.domain.iam.user.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public class UserCriteria {
    public static Specification<User> getUsersIn(Collection<String> userIds) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get(User.Fields.id)).value(userIds);
    }

    public static Specification<User> ofEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get(User.Fields.email), email),
                criteriaBuilder.equal(root.get(User.Fields.role), User.Role.USER)
        );
    }
}
