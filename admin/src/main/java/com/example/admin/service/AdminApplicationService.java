package com.example.admin.service;

import com.example.admin.usecases.ResetPasswordCase;
import com.example.domain.user.model.User;
import com.example.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminApplicationService {
    @Autowired
    private UserService userService;

    public void resetPassword(ResetPasswordCase.Request request, User operator) {
        userService.resetPassword(operator.getId(), request.getPassword(), operator);
        // TODO 是否清理authorize？
    }
}
