package com.example.business.service;

import com.example.domain.auth.model.Authorize;
import com.example.domain.user.model.User;
import com.example.domain.user.service.UserService;
import com.example.business.usecase.user.GetUserDetailCase;
import com.example.business.usecase.user.RegisterCase;
import com.example.business.usecase.user.ResetPasswordCase;
import com.example.business.usecase.user.UpdateUserCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserApplicationService {
    @Autowired
    private UserService service;

    public RegisterCase.Response register(RegisterCase.Request request) {
        // TODO verificationCode
        User user = service.create(request.getName(), request.getEmail(), request.getPassword());

        return RegisterCase.Response.from(user);
    }

    public GetUserDetailCase.Response getDetail(User operator) {
        String userId = operator.getId();
        return GetUserDetailCase.Response.from(service.get(userId));
    }

    public UpdateUserCase.Response update(UpdateUserCase.Request request, User operator) {
        User updatedUser = service.update(operator.getId(), request.getName(), operator);
        return UpdateUserCase.Response.from(updatedUser);
    }


    public void resetPassword(ResetPasswordCase.Request request, User operator) {
        service.resetPassword(operator.getId(), request.getPassword(), operator);
        // TODO 是否清理authorize？
    }
}
