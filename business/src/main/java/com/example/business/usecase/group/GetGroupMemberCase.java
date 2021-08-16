package com.example.business.usecase.group;

import com.example.domain.group.model.GroupMember;
import com.example.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class GetGroupMemberCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String userId;
        private String name;
        private GroupMember.Role role;
        private Instant createdAt;
        private Instant updatedAt;

        public static Response from(GroupMember groupMember, User user) {
            return Response.builder()
                    .id(groupMember.getId())
                    .userId(groupMember.getUserId())
                    .name(user.getName())
                    .role(groupMember.getRole())
                    .createdAt(groupMember.getCreatedAt())
                    .updatedAt(groupMember.getUpdatedAt())
                    .build();
        }
    }
}
