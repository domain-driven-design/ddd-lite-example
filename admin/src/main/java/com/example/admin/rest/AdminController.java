package com.example.admin.rest;

import com.example.admin.service.AdminApplicationService;
import com.example.admin.usecases.authorize.ResetPasswordCase;
import com.example.domain.iam.auth.service.AuthorizeService;
import com.example.domain.iam.user.model.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admins")
public class AdminController {
    @Autowired
    private AdminApplicationService adminApplicationService;
    @Autowired
    private AuthorizeService authorizeService;


    @PutMapping("/password")
    public void resetPassword(@RequestBody ResetPasswordCase.Request request) {
        Operator operator = authorizeService.getOperator();
        adminApplicationService.resetPassword(request, operator);
    }
}
