package com.example.business.usecase.authorize;

import com.example.domain.auth.model.Authorize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class LoginCase {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "email_required")
        private String email;
        @NotBlank(message = "password_required")
        private String password;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String userId;
        private String token;
        private long expire;

        public static Response from(Authorize authorize) {
            return Response.builder()
                    .userId(authorize.getUserId())
                    .token(authorize.getId())
                    .expire(authorize.getExpire())
                    .build();
        }
    }
}
