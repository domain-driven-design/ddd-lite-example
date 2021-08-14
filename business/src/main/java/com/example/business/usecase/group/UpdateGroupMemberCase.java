package com.example.business.usecase.group;

import com.example.domain.group.model.GroupMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class UpdateGroupMemberCase {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "group_member_role_required")
        private GroupMember.Role role;
    }

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
