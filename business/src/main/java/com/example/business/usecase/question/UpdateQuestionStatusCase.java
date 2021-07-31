package com.example.business.usecase.question;

import com.example.domain.question.model.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class UpdateQuestionStatusCase {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "question_status_required")
        private Question.Status status;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private Question.Status status;

        public static Response from(Question question) {
            return Response.builder()
                    .id(question.getId())
                    .status(question.getStatus())
                    .build();
        }
    }
}
