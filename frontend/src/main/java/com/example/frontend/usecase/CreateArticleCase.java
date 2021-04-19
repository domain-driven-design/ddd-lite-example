package com.example.frontend.usecase;

import com.example.domain.article.model.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CreateArticleCase {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String title;
        private String content;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String title;
        private String content;

        public static Response from(Article article) {
            return Response.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .build();
        }
    }
}
