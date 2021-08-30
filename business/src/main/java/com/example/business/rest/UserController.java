package com.example.business.rest;

import com.example.business.service.UserApplicationService;
import com.example.business.usecase.user.GetUserDetailCase;
import com.example.business.usecase.user.RegisterCase;
import com.example.business.usecase.user.ResetPasswordCase;
import com.example.business.usecase.user.UpdateUserCase;
import com.example.domain.auth.service.AuthorizeService;
import com.example.domain.user.model.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserApplicationService applicationService;

    @Autowired
    private AuthorizeService authorizeService;

    @PostMapping
    @ResponseStatus(CREATED)
    public RegisterCase.Response register(@RequestBody @Valid RegisterCase.Request request) {
        return applicationService.register(request);
    }

    @GetMapping("/me")
    public GetUserDetailCase.Response getDetail() {
        Operator operator = authorizeService.getOperator();
        return applicationService.getDetail(operator);
    }

    @PutMapping("/me")
    public UpdateUserCase.Response update(@RequestBody @Valid UpdateUserCase.Request request) {
        Operator operator = authorizeService.getOperator();
        return applicationService.update(request, operator);
    }

    @PutMapping("/me/password")
    public void resetPassword(@RequestBody @Valid ResetPasswordCase.Request request) {
        Operator operator = authorizeService.getOperator();
        applicationService.resetPassword(request, operator);
    }
}
