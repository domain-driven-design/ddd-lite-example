package com.example.frontend.usecase;

import com.example.domain.article.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CreateTagCase {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String name;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String name;

        public static Response from(Tag tag) {
            return Response.builder()
                    .id(tag.getId())
                    .name(tag.getName())
                    .build();
        }
    }
}
