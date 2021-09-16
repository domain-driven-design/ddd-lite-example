package com.example.admin.usecases.group;

import com.example.domain.group.model.Group;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class GetGroupsCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;

        private String name;

        private String description;

        private Instant createdAt;

        public static Response from(Group group) {
            return Response.builder()
                    .id(group.getId())
                    .name(group.getName())
                    .description(group.getDescription())
                    .createdAt(group.getCreatedAt())
                    .build();
        }
    }

}
