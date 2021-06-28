package com.example.business.usecase.question;

import com.example.domain.question.model.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class UpdateAnswerCase {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "answer_content_required")
        private String content;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String content;

        public static Response from(Answer answer) {
            return Response.builder()
                    .id(answer.getId())
                    .content(answer.getContent())
                    .build();
        }
    }
}
