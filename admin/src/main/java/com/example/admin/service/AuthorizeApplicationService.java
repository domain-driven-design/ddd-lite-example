package com.example.admin.service;

import com.example.admin.usecases.LoginCase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.auth.service.AuthorizeService;
import com.example.domain.user.exception.UserException;
import com.example.domain.user.model.User;
import com.example.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeApplicationService {
    @Autowired
    private AuthorizeService service;
    @Autowired
    private UserRepository userRepository;

    public LoginCase.Response login(LoginCase.Request request) {
        User user = userRepository.findOne(Example.of(User.builder()
                .name(request.getName())
                .password(request.getPassword())
                .role(User.UserRole.ADMIN)
                .build()))
                .orElseThrow(UserException::notFound);

        Authorize authorize = service.create(user);
        return LoginCase.Response.from(authorize);
    }

    public void logout() {
        Authorize authorize = service.current();
        service.delete(authorize.getId());
    }
}
