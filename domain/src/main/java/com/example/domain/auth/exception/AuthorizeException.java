package com.example.domain.auth.exception;

import com.example.domain.common.BaseException;

public class AuthorizeException extends BaseException {
    public AuthorizeException(Type type, String message) {
        super(type, message);
    }

    public static AuthorizeException Unauthorized() {
        return new AuthorizeException(BaseException.Type.UNAUTHORIZED, "unauthorized");
    }

    public static AuthorizeException invalidCredential() {
        return new AuthorizeException(BaseException.Type.UNAUTHORIZED, "invalid_credential");
    }

    public static AuthorizeException userFrozen() {
        return new AuthorizeException(Type.BAD_REQUEST, "user_frozen");
    }
}
