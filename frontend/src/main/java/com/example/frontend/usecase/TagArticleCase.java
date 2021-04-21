package com.example.frontend.usecase;

import com.example.domain.article.model.ArticleTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

public class TagArticleCase {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String tagId;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String tagId;
        private String articleId;
        private Instant createdAt;

        public static Response from(ArticleTag articleTag) {
            return Response.builder()
                    .tagId(articleTag.getTagId())
                    .articleId(articleTag.getArticleId())
                    .createdAt(articleTag.getCreatedAt())
                    .build();
        }
    }
}
