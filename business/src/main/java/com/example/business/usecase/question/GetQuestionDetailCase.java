package com.example.business.usecase.question;

import com.example.domain.question.model.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class GetQuestionDetailCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String title;
        private String description;
        private String createdBy;
        private Instant createdAt;
        private Instant updatedAt;

        public static Response from(Question question) {
            return Response.builder()
                    .id(question.getId())
                    .title(question.getTitle())
                    .description(question.getDescription())
                    .createdBy(question.getCreatedBy())
                    .createdAt(question.getCreatedAt())
                    .updatedAt(question.getUpdatedAt())
                    .build();
        }
    }
}
