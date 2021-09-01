package com.example.admin.service;

import com.example.admin.usecases.LoginCase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.auth.service.AuthorizeService;
import com.example.domain.user.model.User;
import com.example.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthorizeApplicationService {
    @Autowired
    private AuthorizeService service;

    @Autowired
    private UserService userService;

    public LoginCase.Response login(LoginCase.Request request) {
        User user = userService.get(Example.of(User.builder()
                .name(request.getName())
                .role(User.Role.ADMIN)
                .build()));

        Authorize authorize = service.create(user, request.getPassword());
        return LoginCase.Response.from(authorize);
    }

    public void logout() {
        Authorize authorize = service.getCurrent();
        service.delete(authorize.getId());
    }
}
