package com.example.domain.user.exception;

public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }

    public static UserException emailConflicted() {
        return new UserException("email_conflicted");
    }

    public static UserException notFound() {
        return new UserException("user_not_found");
    }
}
