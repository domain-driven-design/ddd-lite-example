package com.example.business.service;

import com.example.domain.auth.model.Authorize;
import com.example.domain.user.model.Operator;
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

    public GetUserDetailCase.Response getDetail(Operator operator) {
        String userId = operator.getUserId();
        return GetUserDetailCase.Response.from(service.get(userId));
    }

    public UpdateUserCase.Response update(UpdateUserCase.Request request, Operator operator) {
        User updatedUser = service.update(operator.getUserId(), request.getName(), operator);
        return UpdateUserCase.Response.from(updatedUser);
    }


    public void resetPassword(ResetPasswordCase.Request request, Operator operator) {
        service.resetPassword(operator.getUserId(), request.getPassword(), operator);
        // TODO clear authorize？
    }
}
