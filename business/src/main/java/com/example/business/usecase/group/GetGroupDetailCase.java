package com.example.business.usecase.group;

import com.example.business.usecase.common.CreatorResponse;
import com.example.domain.group.model.Group;
import com.example.domain.group.model.GroupMember;
import com.example.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class GetGroupDetailCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String name;
        private String description;
        private List<GroupMemberResponse> members;
        private CreatorResponse creator;
        private Instant createdAt;
        private Instant updatedAt;

        public static Response from(Group group, User creator) {
            return Response.builder()
                    .id(group.getId())
                    .name(group.getName())
                    .description(group.getDescription())
                    .members(group.getMembers().stream().map(GroupMemberResponse::from).collect(Collectors.toList()))
                    .creator(CreatorResponse.from(creator))
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
        private Instant createdAt;
        private GroupMember.Role role;

        public static GroupMemberResponse from(GroupMember groupMember) {
            return GroupMemberResponse.builder()
                    .userId(groupMember.getUserId())
                    .createdAt(groupMember.getCreatedAt())
                    .role(groupMember.getRole())
                    .build();
        }
    }
}
