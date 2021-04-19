package com.example.frontend.service;

import com.example.domain.user.model.User;
import com.example.domain.user.service.UserService;
import com.example.frontend.usecase.GetUserDetailCase;
import com.example.frontend.usecase.RegisterCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserApplicationService {
    private final UserService service;

    public GetUserDetailCase.Response getUserDetail(String id) {
        User user = service.getById(id);

        return GetUserDetailCase.Response.from(user);
    }

    public RegisterCase.Response register(RegisterCase.Request request) {
        User user = service.create(request.getName(), request.getEmail(), request.getPassword());

        return RegisterCase.Response.from(user);
    }
}
