package com.example.domain.article.exception;

public class TagArticleException extends RuntimeException {
    public TagArticleException(String message) {
        super(message);
    }

    public static TagArticleException alreadyExist() {
        return new TagArticleException("tag_article_already_exist");
    }
}
