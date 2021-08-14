package com.example.business.usecase.group;

import com.example.domain.group.model.GroupMember;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class JoinGroupCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String groupId;
        private String userId;
        private GroupMember.Role role;

        public static Response from(GroupMember groupMember) {
            return Response.builder()
                    .groupId(groupMember.getGroupId())
                    .userId(groupMember.getUserId())
                    .role(groupMember.getRole())
                    .build();
        }
    }
}
