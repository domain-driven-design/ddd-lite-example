package com.example.frontend.usecase;

import com.example.domain.article.model.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class GetArticlesCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String title;
        private String content;
        private Instant lastModifiedAt;

        public static Response from(Article article) {
            return Response.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .lastModifiedAt(article.getLastModifiedAt())
                    .build();
        }
    }
}
