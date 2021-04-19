package com.example.domain.article.exception;

public class ArticleException extends RuntimeException {
    public ArticleException(String message) {
        super(message);
    }

    public static ArticleException notFound() {
        return new ArticleException("article_not_found");
    }
}
