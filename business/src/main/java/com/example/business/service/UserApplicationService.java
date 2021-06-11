package com.example.business.service;

import com.example.domain.auth.model.Authorize;
import com.example.domain.user.model.User;
import com.example.domain.user.service.UserService;
import com.example.business.usecase.GetUserDetailCase;
import com.example.business.usecase.RegisterCase;
import com.example.business.usecase.ResetPasswordCase;
import com.example.business.usecase.UpdateUserCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserApplicationService {
    @Autowired
    private UserService service;

    public RegisterCase.Response register(RegisterCase.Request request) {
        // TODO 验证凭据信息是否可用，如：email
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
