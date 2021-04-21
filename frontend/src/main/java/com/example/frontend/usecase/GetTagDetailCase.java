package com.example.frontend.usecase;

import com.example.domain.article.model.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class GetTagDetailCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String name;
        private Instant createdAt;
        private Instant updatedAt;

        public static Response from(Tag tag) {
            return Response.builder()
                    .id(tag.getId())
                    .name(tag.getName())
                    .createdAt(tag.getCreatedAt())
                    .updatedAt(tag.getUpdatedAt())
                    .build();
        }
    }
}
