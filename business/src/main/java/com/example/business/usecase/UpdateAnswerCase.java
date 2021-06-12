package com.example.business.usecase;

import com.example.domain.question.model.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UpdateAnswerCase {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
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
