package com.example.frontend.rest;

import com.example.frontend.service.UserApplicationService;
import com.example.frontend.usecase.GetUserDetailCase;
import com.example.frontend.usecase.RegisterCase;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserApplicationService applicationService;

    @PostMapping
    @ResponseStatus(CREATED)
    public RegisterCase.Response register(@RequestBody RegisterCase.Request request) {
        return applicationService.register(request);
    }

    @GetMapping("/{id}")
    public GetUserDetailCase.Response getUserDetail(@PathVariable("id") String id) {
        return applicationService.getUserDetail(id);
    }
}
