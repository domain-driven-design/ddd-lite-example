package com.example.business.usecase.question;

import com.example.business.usecase.common.CreatorResponse;
import com.example.domain.question.model.Question;
import com.example.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class GetManagementQuestionCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String title;
        private String description;
        private CreatorResponse creator;
        private Instant createdAt;
        private Instant updatedAt;

        public static Response from(Question question, User creator) {
            return Response.builder()
                    .id(question.getId())
                    .title(question.getTitle())
                    .description(question.getDescription())
                    .creator(CreatorResponse.from(creator))
                    .createdAt(question.getCreatedAt())
                    .updatedAt(question.getUpdatedAt())
                    .build();
        }
    }
}
