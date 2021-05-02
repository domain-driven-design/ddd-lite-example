package com.example.domain.auth.exception;

public class AuthorizeException extends RuntimeException {
    public AuthorizeException(String message) {
        super(message);
    }

    public static AuthorizeException Unauthorized() {
        return new AuthorizeException("unauthorized");
    }
}
