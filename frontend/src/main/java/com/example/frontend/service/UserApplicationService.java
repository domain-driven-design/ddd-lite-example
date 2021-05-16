package com.example.frontend.service;

import com.example.domain.auth.model.Authorize;
import com.example.domain.user.model.User;
import com.example.domain.user.service.UserService;
import com.example.frontend.usecase.GetUserDetailCase;
import com.example.frontend.usecase.RegisterCase;
import com.example.frontend.usecase.ResetPasswordCase;
import com.example.frontend.usecase.UpdateUserCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserApplicationService {
    @Autowired
    private UserService service;

    public RegisterCase.Response register(RegisterCase.Request request) {
        User user = service.create(request.getName(), request.getEmail(), request.getPassword());

        return RegisterCase.Response.from(user);
    }

    public GetUserDetailCase.Response getDetail(Authorize authorize) {
        String userId = authorize.getUserId();
        return GetUserDetailCase.Response.from(service.get(userId));
    }

    public UpdateUserCase.Response update(UpdateUserCase.Request request, Authorize authorize) {
        User updatedUser = service.update(authorize.getUserId(), request.getName(), authorize.getUserId());
        return UpdateUserCase.Response.from(updatedUser);
    }


    public void resetPassword(ResetPasswordCase.Request request, Authorize authorize) {
        service.resetPassword(authorize.getUserId(), request.getPassword(), authorize.getUserId());
        // TODO 是否清理authorize？
    }
}
