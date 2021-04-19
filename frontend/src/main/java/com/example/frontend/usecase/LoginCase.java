package com.example.frontend.usecase;

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
        private String email;
        private String password;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String token;
        private long expire;

        public static Response from(Authorize authorize) {
            return Response.builder()
                    .token(authorize.getId())
                    .expire(authorize.getExpire())
                    .build();
        }
    }
}
