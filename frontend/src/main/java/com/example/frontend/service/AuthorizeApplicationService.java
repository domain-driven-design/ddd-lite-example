package com.example.frontend.service;

import com.example.domain.auth.model.Authorize;
import com.example.domain.auth.service.AuthorizeService;
import com.example.domain.user.exception.UserException;
import com.example.domain.user.model.User;
import com.example.domain.user.repository.UserRepository;
import com.example.frontend.usecase.LoginCase;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthorizeApplicationService {
    private final AuthorizeService service;
    private final UserRepository userRepository;

    public LoginCase.Response login(LoginCase.Request request) {
        User user = userRepository.findOne(Example.of(User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build()))
                .orElseThrow(UserException::notFound);

        Authorize authorize = service.create(user);
        return LoginCase.Response.from(authorize);
    }
}
