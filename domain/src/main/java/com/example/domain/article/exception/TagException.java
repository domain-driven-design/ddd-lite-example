package com.example.domain.article.exception;

public class TagException extends RuntimeException {
    public TagException(String message) {
        super(message);
    }

    public static TagException notFound() {
        return new TagException("tag_not_found");
    }
}
