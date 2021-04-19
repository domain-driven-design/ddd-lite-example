package com.example.frontend.rest;

import com.example.frontend.service.AuthorizeApplicationService;
import com.example.frontend.usecase.LoginCase;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("authorizes")
@AllArgsConstructor
public class AuthorizeController {
    private final AuthorizeApplicationService applicationService;

    @PostMapping
    @ResponseStatus(CREATED)
    public LoginCase.Response login(@RequestBody LoginCase.Request request) {
        return applicationService.login(request);
    }
}
