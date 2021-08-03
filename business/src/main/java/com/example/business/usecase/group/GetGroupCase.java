package com.example.business.usecase.group;

import com.example.domain.group.model.Group;
import com.example.domain.group.model.GroupMember;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class GetGroupCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String name;
        private String description;
        private List<GroupMemberResponse> members;
        private String createdBy;
        private Instant createdAt;
        private Instant updatedAt;

        public static Response from(Group group) {
            return Response.builder()
                    .id(group.getId())
                    .name(group.getName())
                    .description(group.getDescription())
                    .members(group.getMembers().stream().map(GroupMemberResponse::from).collect(Collectors.toList()))
                    .createdBy(group.getCreatedBy())
                    .createdAt(group.getCreatedAt())
                    .updatedAt(group.getUpdatedAt())
                    .build();
        }
    }


    @Getter
    @Setter
    @Builder
    public static class GroupMemberResponse {
        private String userId;
        private String createdBy;
        private Instant createdAt;
        private Instant updatedAt;

        public static GroupMemberResponse from(GroupMember groupMember) {
            return GroupMemberResponse.builder()
                    .userId(groupMember.getUserId())
                    .createdBy(groupMember.getCreatedBy())
                    .createdAt(groupMember.getCreatedAt())
                    .updatedAt(groupMember.getUpdatedAt())
                    .build();
        }
    }
}
