package com.example.admin.service;

import com.example.admin.usecases.CreateAdminCase;
import com.example.admin.usecases.GetUserDetailCase;
import com.example.admin.usecases.GetUsersCase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.user.model.User;
import com.example.domain.user.repository.UserRepository;
import com.example.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManagementApplicationService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService userService;

    public List<GetUsersCase.Response> getUsers() {
        return repository.findAll().stream()
                .map(GetUsersCase.Response::from)
                .collect(Collectors.toList());
    }

    public GetUserDetailCase.Response getUserDetail(String id) {
        User user = repository.getOne(id);
        return GetUserDetailCase.Response.from(user);
    }

    public CreateAdminCase.Response createAdmin(CreateAdminCase.Request request, Authorize authorize) {
        User admin = userService.createAdmin(request.getName(), authorize.getUserId());
        return CreateAdminCase.Response.from(admin);
    }
}
