package com.example.business.usecase.authorize;

import com.example.domain.auth.model.Authorize;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class GetUserProfileCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String userId;
        private Long expire;

        public static Response from(Authorize authorize) {
            return Response.builder()
                    .id(authorize.getId())
                    .userId(authorize.getUserId())
                    .expire(authorize.getExpire())
                    .build();
        }
    }
}
