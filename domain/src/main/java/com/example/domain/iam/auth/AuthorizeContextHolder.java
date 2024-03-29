package com.example.domain.iam.auth;

import com.example.domain.iam.auth.model.Authorize;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizeContextHolder {
    private static final ThreadLocal<Authorize> contextHolder = new ThreadLocal<>();

    public static Authorize getContext() {
        return contextHolder.get();
    }

    public static void setContext(Authorize context) {
        contextHolder.set(context);
    }

}
