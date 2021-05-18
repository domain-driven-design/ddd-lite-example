package com.example.admin.usecases;

import com.example.domain.auth.model.Authorize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class LoginCase {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String name;
        private String password;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String userId;
        private String token;

        public static Response from(Authorize authorize) {
            return Response.builder()
                    .userId(authorize.getUserId())
                    .token(authorize.getId())
                    .build();
        }
    }
}
