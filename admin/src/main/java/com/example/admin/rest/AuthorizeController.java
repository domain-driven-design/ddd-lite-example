package com.example.admin.rest;

import com.example.admin.service.AuthorizeApplicationService;
import com.example.admin.usecases.LoginCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/authorizes/admin")
public class AuthorizeController {
    @Autowired
    private AuthorizeApplicationService authorizeApplicationService;

    @PostMapping
    @ResponseStatus(CREATED)
    public LoginCase.Response login(@RequestBody LoginCase.Request request) {
        return authorizeApplicationService.login(request);
    }

    @DeleteMapping
    public void logout() {
        authorizeApplicationService.logout();
    }
}
