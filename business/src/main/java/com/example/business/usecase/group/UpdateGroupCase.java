package com.example.business.usecase.group;

import com.example.domain.group.model.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class UpdateGroupCase {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "group_name_required")
        private String name;
        @NotBlank(message = "group_description_required")
        private String description;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String name;
        private String description;

        public static Response from(Group group) {
            return Response.builder()
                    .id(group.getId())
                    .name(group.getName())
                    .description(group.getDescription())
                    .build();
        }
    }
}
