package com.example.admin.usecases.question;

import com.example.admin.usecases.common.CreatorResponse;
import com.example.domain.group.model.Group;
import com.example.domain.question.model.Question;
import com.example.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class GetQuestionsCase {
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String title;
        private String description;
        private Question.Status status;
        private Instant createdAt;
        private GroupResponse group;
        private CreatorResponse creator;

        public static Response from(Question question, User creator, Group group) {
            return Response.builder()
                    .id(question.getId())
                    .title(question.getTitle())
                    .description(question.getDescription())
                    .status(question.getStatus())
                    .createdAt(question.getCreatedAt())
                    .creator(CreatorResponse.from(creator))
                    .group(question.getGroupId().equals(Group.DEFAULT)
                            ? GroupResponse.defaultGroup()
                            : GroupResponse.from(group)
                    )
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    public static class GroupResponse {
        String id;
        String name;

        public static GroupResponse defaultGroup() {
            return GroupResponse.builder()
                    .id(Group.DEFAULT)
                    .build();
        }

        public static GroupResponse from(Group group) {
            if (group == null) {
                return null;
            }
            return GroupResponse.builder()
                    .id(group.getId())
                    .name(group.getName())
                    .build();
        }

    }

}
