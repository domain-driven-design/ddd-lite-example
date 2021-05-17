package com.example.admin.service;

import com.example.admin.usecases.CreateAdminCase;
import com.example.admin.usecases.GetUserDetailCase;
import com.example.admin.usecases.GetUsersCase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.user.model.User;
import com.example.domain.user.repository.UserRepository;
import com.example.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserManagementApplicationService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService userService;

    public Page<GetUsersCase.Response> getUsers(Pageable pageable) {
        return repository.findAll(Example.of(User.builder().role(User.UserRole.USER).build()), pageable)
                .map(GetUsersCase.Response::from);
    }

    public GetUserDetailCase.Response getUserDetail(String id) {
        User user = repository.getOne(id);
        return GetUserDetailCase.Response.from(user);
    }

    public CreateAdminCase.Response createAdmin(CreateAdminCase.Request request, Authorize authorize) {
        User admin = userService.createAdmin(request.getName(), request.getPassword(), authorize.getUserId());
        return CreateAdminCase.Response.from(admin);
    }
}
