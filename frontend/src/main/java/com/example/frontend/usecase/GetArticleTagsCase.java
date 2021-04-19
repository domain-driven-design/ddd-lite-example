package com.example.frontend.usecase;

import com.example.domain.article.model.TagArticle;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class GetArticleTagsCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String name;
        private Instant bindAt;

        public static Response from(TagArticle tagArticle) {
            return Response.builder()
                    .id(tagArticle.getTag().getId())
                    .name(tagArticle.getTag().getName())
                    .bindAt(tagArticle.getLastModifiedAt())
                    .build();
        }
    }
}
