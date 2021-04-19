package com.example.frontend.usecase;

import com.example.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class RegisterCase {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String name;
        private String email;
        private String password;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String name;
        private String email;

        public static Response from(User user) {
            return Response.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();
        }
    }
}
