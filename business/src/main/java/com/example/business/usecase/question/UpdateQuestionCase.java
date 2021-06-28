package com.example.business.usecase.question;

import com.example.domain.question.model.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class UpdateQuestionCase {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "question_title_required")
        private String title;
        private String description;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String title;
        private String description;

        public static Response from(Question question) {
            return Response.builder()
                    .id(question.getId())
                    .title(question.getTitle())
                    .description(question.getDescription())
                    .build();
        }
    }
}
