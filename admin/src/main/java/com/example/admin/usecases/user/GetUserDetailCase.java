package com.example.admin.usecases.user;

import com.example.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class GetUserDetailCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String name;
        private String email;
        private Instant createdAt;

        public static Response from(User user) {
            return Response.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .createdAt(user.getCreatedAt())
                    .build();
        }
    }
}
