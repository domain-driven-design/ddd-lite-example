package com.example.business.usecase.question;

import com.example.business.usecase.common.CreatorResponse;
import com.example.domain.question.model.Answer;
import com.example.domain.iam.user.model.User;
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
        private CreatorResponse creator;
        private Instant createdAt;
        private Instant updatedAt;

        public static Response from(Answer answer, User creator) {
            return Response.builder()
                    .id(answer.getId())
                    .content(answer.getContent())
                    .creator(CreatorResponse.from(creator))
                    .createdAt(answer.getCreatedAt())
                    .updatedAt(answer.getUpdatedAt())
                    .build();
        }
    }
}
