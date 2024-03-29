package com.example.admin.common;

import com.example.domain.iam.user.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public class UserCriteria {
    public static Specification<User> getUsersIn(Collection<String> userIds) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get(User.Fields.id)).value(userIds);
    }
}
