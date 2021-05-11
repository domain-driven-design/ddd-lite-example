package com.example.frontend.service;

import com.example.domain.user.model.User;
import com.example.domain.user.service.UserService;
import com.example.frontend.usecase.RegisterCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserApplicationService {
    @Autowired
    private UserService service;

    public RegisterCase.Response register(RegisterCase.Request request) {
        User user = service.create(request.getName(), request.getEmail(), request.getPassword());

        return RegisterCase.Response.from(user);
    }
}
