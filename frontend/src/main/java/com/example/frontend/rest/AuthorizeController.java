package com.example.frontend.rest;

import com.example.frontend.service.AuthorizeApplicationService;
import com.example.frontend.usecase.GetUserProfileCase;
import com.example.frontend.usecase.LoginCase;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/authorizes")
@AllArgsConstructor
public class AuthorizeController {
    private final AuthorizeApplicationService authorizeApplicationService;

    @PostMapping
    @ResponseStatus(CREATED)
    public LoginCase.Response login(@RequestBody LoginCase.Request request) {
        return authorizeApplicationService.login(request);
    }

    @DeleteMapping
    public void logout() {
        authorizeApplicationService.logout();
    }

    @GetMapping("/me")
    public GetUserProfileCase.Response getMyProfile() {
        return authorizeApplicationService.getProfile();
    }
}
