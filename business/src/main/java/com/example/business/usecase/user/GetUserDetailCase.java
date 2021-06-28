package com.example.business.usecase.user;

import com.example.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class GetUserDetailCase {
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
