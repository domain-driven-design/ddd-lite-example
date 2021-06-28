package com.example.business.usecase.group;

import com.example.domain.group.model.Group;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class GetMyGroupCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String name;
        private String createdBy;
        private Instant createdAt;
        private Instant updatedAt;

        public static Response from(Group group) {
            return Response.builder()
                    .id(group.getId())
                    .name(group.getName())
                    .createdBy(group.getCreatedBy())
                    .createdAt(group.getCreatedAt())
                    .updatedAt(group.getUpdatedAt())
                    .build();
        }
    }
}
