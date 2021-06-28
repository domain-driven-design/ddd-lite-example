package com.example.business.usecase.question;

import com.example.domain.question.model.Answer;
import com.example.domain.question.model.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class GetAnswerCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String content;
        private String createdBy;
        private Instant createdAt;
        private Instant updatedAt;

        public static Response from(Answer answer) {
            return Response.builder()
                    .id(answer.getId())
                    .content(answer.getContent())
                    .createdBy(answer.getCreatedBy())
                    .createdAt(answer.getCreatedAt())
                    .updatedAt(answer.getUpdatedAt())
                    .build();
        }
    }
}
