package com.example.admin.service;

import com.example.admin.usecases.user.CreateUserCase;
import com.example.admin.usecases.user.GetUserDetailCase;
import com.example.admin.usecases.user.GetUsersCase;
import com.example.admin.usecases.user.UpdateUserStatusCase;
import com.example.domain.user.model.Operator;
import com.example.domain.user.model.User;
import com.example.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserManagementApplicationService {
    @Autowired
    private UserService userService;

    public Page<GetUsersCase.Response> getUsers(String keyword, Pageable pageable) {
        Specification<User> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteriaBuilder.equal(root.get(User.Fields.role), User.Role.USER));

            if (keyword != null) {
                List<Predicate> keywordPredicateList = new ArrayList<>();

                keywordPredicateList.add(criteriaBuilder.like(root.get(User.Fields.name), '%' + keyword + '%'));
                keywordPredicateList.add(criteriaBuilder.like(root.get(User.Fields.email), '%' + keyword + '%'));
                predicateList.add(criteriaBuilder.or(keywordPredicateList.toArray(new Predicate[0])));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
        return userService.findAll(spec, pageable)
                .map(GetUsersCase.Response::from);
    }

    public GetUserDetailCase.Response getUserDetail(String id) {
        User user = userService.get(id);
        return GetUserDetailCase.Response.from(user);
    }

    public CreateUserCase.Response createUser(CreateUserCase.Request request, Operator operator) {
        User user = userService.create(request.getName(), request.getEmail(), request.getPassword());
        return CreateUserCase.Response.from(user);
    }

    public UpdateUserStatusCase.Response updateUserStatus(String id,
                                                          UpdateUserStatusCase.Request request,
                                                          Operator operator) {
        User user = userService.updateStatus(id, request.getStatus(), operator);
        return UpdateUserStatusCase.Response.from(user);
    }
}
