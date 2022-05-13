package com.example.domain.group.model;

import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Getter
@Entity
@Table(name = "group_member")
@FieldNameConstants
public class GroupMember {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    @Column(name = "group_id")
    private String groupId;

    private String userId;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String createdBy;

    private Instant createdAt;

    private Instant updatedAt;

    public void setRole(Role role) {
        this.role = role;
        this.updatedAt = Instant.now();
    }

    public enum Role {
        NORMAL, ADMIN, OWNER
    }

    public static GroupMember build(String groupId, String userId, Role role, String createdBy) {
        GroupMember groupMember = new GroupMember();

        groupMember.groupId = groupId;
        groupMember.userId = userId;
        groupMember.role = role;
        groupMember.createdBy = createdBy;
        groupMember.createdAt = Instant.now();
        groupMember.updatedAt = Instant.now();

        return groupMember;
    }
}
