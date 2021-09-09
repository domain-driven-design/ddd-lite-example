package com.example.admin.usecases;

import com.example.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class GetUsersCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String name;
        private String email;
        private User.Status status;
        private Instant createdAt;

        public static Response from(User user) {
            return Response.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .status(user.getStatus())
                    .createdAt(user.getCreatedAt())
                    .build();
        }
    }
}
