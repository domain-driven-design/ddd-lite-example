package com.example.admin.common;

import com.example.domain.group.model.Group;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public class GroupCriteria {
    public static Specification<Group> getGroupsIn(Collection<String> groupIds) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get(Group.Fields.id)).value(groupIds);
    }
}
