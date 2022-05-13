package com.example.domain.auth.model;

import com.example.domain.user.model.User;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Authorize {
    private String id;
    private String userId;
    private User.Role role;
    private Long expire;

    public static Authorize build(String userId, User.Role role) {
        Authorize authorize = new Authorize();

        authorize.id = UUID.randomUUID().toString();
        authorize.userId = userId;
        authorize.role = role;

        return authorize;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
}
