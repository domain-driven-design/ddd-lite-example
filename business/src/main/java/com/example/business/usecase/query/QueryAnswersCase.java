package com.example.business.usecase.query;

import com.example.domain.question.model.Answer;
import com.example.domain.question.model.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class QueryAnswersCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String questionId;
        private String questionTitle;
        private String content;
        private Instant updatedAt;

        public static Response from(Question question, Answer answer) {
            return Response.builder()
                    .questionId(question.getId())
                    .questionTitle(question.getTitle())
                    .content(answer.getContent())
                    .updatedAt(answer.getUpdatedAt())
                    .build();
        }
    }
}
