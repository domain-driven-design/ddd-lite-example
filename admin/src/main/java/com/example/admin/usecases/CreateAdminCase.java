package com.example.admin.usecases;

import com.example.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CreateAdminCase {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String name;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String name;
        private User.UserRole role;

        public static Response from(User user) {
            return Response.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .role(user.getRole())
                    .build();
        }
    }
}
