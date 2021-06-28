package com.example.business.usecase.user;

import com.example.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class UpdateUserCase {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "name_required")
        private String name;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String name;

        public static Response from(User user) {
            return Response.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .build();
        }
    }
}
