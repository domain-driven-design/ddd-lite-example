package com.example.frontend.usecase;

import com.example.domain.article.model.ArticleTag;
import com.example.domain.article.model.Tag;
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

        public static Response from(ArticleTag articleTag, Tag tag) {
            return Response.builder()
                    .id(tag.getId())
                    .name(tag.getName())
                    .bindAt(articleTag.getLastModifiedAt())
                    .build();
        }
    }
}
