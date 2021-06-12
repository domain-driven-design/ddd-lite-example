package com.example.admin.service;

import com.example.admin.usecases.ResetPasswordCase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminApplicationService {
    @Autowired
    private UserService userService;

    public void resetPassword(ResetPasswordCase.Request request, Authorize authorize) {
        userService.resetPassword(authorize.getUserId(), request.getPassword(), authorize.getUserId());
        // TODO 是否清理authorize？
    }
}
