package com.example.admin.usecases.user;

import com.example.domain.iam.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class UpdateUserStatusCase {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "status_required")
        private User.Status status;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private User.Status status;

        public static Response from(User user) {
            return Response.builder()
                    .id(user.getId())
                    .status(user.getStatus())
                    .build();
        }
    }
}
