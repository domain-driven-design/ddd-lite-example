package com.example.frontend.rest;

import com.example.frontend.service.UserApplicationService;
import com.example.frontend.usecase.RegisterCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserApplicationService applicationService;

    @PostMapping
    @ResponseStatus(CREATED)
    public RegisterCase.Response register(@RequestBody RegisterCase.Request request) {
        return applicationService.register(request);
    }
}
