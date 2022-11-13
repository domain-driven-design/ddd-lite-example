package com.example.admin.service;

import com.example.admin.usecases.authorize.ResetPasswordCase;
import com.example.domain.iam.user.model.Operator;
import com.example.domain.iam.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminApplicationService {
    @Autowired
    private UserService userService;

    public void resetPassword(ResetPasswordCase.Request request, Operator operator) {
        userService.resetPassword(operator.getUserId(), request.getPassword(), operator);
    }
}
