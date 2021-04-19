package com.example.frontend.usecase;

import com.example.domain.article.model.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class GetTagsCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String name;
        private Instant lastModifiedAt;

        public static Response from(Tag tag) {
            return Response.builder()
                    .id(tag.getId())
                    .name(tag.getName())
                    .lastModifiedAt(tag.getLastModifiedAt())
                    .build();
        }
    }
}
