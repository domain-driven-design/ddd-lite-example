package com.example.domain.group.model;

import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.util.List;

@Getter
@Entity
@Table(name = "`group`")
@FieldNameConstants
public class Group {
    public static final String DEFAULT = "default";

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    private String name;

    private String description;

    private String createdBy;

    private Instant createdAt;

    private Instant updatedAt;

    @OneToMany
    @JoinColumn(name = "group_id")
    private List<GroupMember> members;

    public static Group build(String name, String description, String createdBy) {
        Group group = new Group();

        group.name = name;
        group.description = description;
        group.createdBy = createdBy;
        group.createdAt = Instant.now();
        group.updatedAt = Instant.now();

        return group;
    }

    public void setMembers(List<GroupMember> members) {
        this.members = members;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = Instant.now();
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = Instant.now();
    }
}
