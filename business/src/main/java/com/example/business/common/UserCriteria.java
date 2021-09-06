package com.example.business.common;

import com.example.domain.user.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public class UserCriteria {
    public static Specification<User> getUsersIn(Collection<String> userIds) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get(User.Fields.id)).value(userIds);
    }
}
