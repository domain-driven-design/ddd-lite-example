package com.example.business.service;

import com.example.domain.auth.model.Authorize;
import com.example.domain.auth.service.AuthorizeService;
import com.example.domain.user.model.User;
import com.example.domain.user.service.UserService;
import com.example.business.usecase.authorize.GetUserProfileCase;
import com.example.business.usecase.authorize.LoginCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeApplicationService {
    @Autowired
    private AuthorizeService service;

    @Autowired
    private UserService userService;

    public LoginCase.Response login(LoginCase.Request request) {
        User user = userService.get(Example.of(User.builder()
                .email(request.getEmail())
                .role(User.UserRole.USER)
                .build()));

        Authorize authorize = service.create(user, request.getPassword());
        return LoginCase.Response.from(authorize);
    }

    public void logout() {
        Authorize authorize = service.getCurrent();
        service.delete(authorize.getId());
    }

    public GetUserProfileCase.Response getProfile() {
        Authorize authorize = service.getCurrent();
        return GetUserProfileCase.Response.from(authorize);
    }
}
