package com.example.business.service;

import com.example.business.usecase.group.ChangeGroupOwnerCase;
import com.example.business.usecase.group.CreateGroupCase;
import com.example.business.usecase.group.GetGroupCase;
import com.example.business.usecase.group.GetGroupDetailCase;
import com.example.business.usecase.group.GetGroupMemberCase;
import com.example.business.usecase.group.GetMyGroupCase;
import com.example.business.usecase.group.JoinGroupCase;
import com.example.business.usecase.group.UpdateGroupCase;
import com.example.business.usecase.group.UpdateGroupMemberCase;
import com.example.domain.group.model.Group;
import com.example.domain.group.model.GroupMember;
import com.example.domain.group.service.GroupService;
import com.example.domain.user.model.User;
import com.example.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
public class GroupApplicationService {
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;

    public CreateGroupCase.Response createGroup(CreateGroupCase.Request request, User operator) {
        Group group = groupService.create(request.getName(), request.getDescription(), operator);
        return CreateGroupCase.Response.from(group);
    }

    public GetGroupDetailCase.Response getGroupDetail(String id) {
        Group group = groupService.get(id);

        User creator = userService.get(group.getCreatedBy());

        return GetGroupDetailCase.Response.from(group, creator);
    }

    public Page<GetGroupCase.Response> getGroupsByPage(Pageable pageable) {
        Page<Group> groupPage = groupService.findAll(null, pageable);

        return groupPage.map(GetGroupCase.Response::from);
    }

    public Page<GetMyGroupCase.Response> getMineGroupsByPage(Pageable pageable, User operator) {
        Specification<Group> specification = (root, query, criteriaBuilder) -> {
            Join<Group, List<GroupMember>> join = root.join(Group.Fields.members, JoinType.LEFT);
            return criteriaBuilder.equal(join.get(GroupMember.Fields.userId), operator.getId());
        };
        Page<Group> groupPage = groupService.findAll(specification, pageable);

        return groupPage.map(GetMyGroupCase.Response::from);
    }

    public UpdateGroupCase.Response updateGroup(String id, UpdateGroupCase.Request request, User operator) {
        Group group = groupService.update(id, request.getName(), request.getDescription(), operator);
        return UpdateGroupCase.Response.from(group);
    }

    public Page<GetGroupMemberCase.Response> getGroupMembers(String id, Pageable pageable) {
        Specification<GroupMember> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(GroupMember.Fields.groupId), id);
        Page<GroupMember> page = groupService.findAllMembers(specification, pageable);

        Set<String> userIds = page.getContent().stream().map(GroupMember::getUserId).collect(Collectors.toSet());
        Specification<User> userSpecification = (root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get(User.Fields.id)).value(userIds);

        Map<String, User> userMap = userService.findAll(userSpecification, Pageable.unpaged()).getContent().stream()
                .collect(toMap(User::getId, Function.identity()));

        return page.map(groupMember ->
                GetGroupMemberCase.Response.from(groupMember, userMap.get(groupMember.getUserId()))
        );
    }

    public Page<GetGroupMemberCase.Response> getGroupManagementMembers(String id, Pageable pageable) {
        Specification<GroupMember> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get(GroupMember.Fields.groupId), id),
                        criteriaBuilder.notEqual(root.get(GroupMember.Fields.role), GroupMember.Role.OWNER)
                );
        Page<GroupMember> page = groupService.findAllMembers(specification, pageable);

        Set<String> userIds = page.getContent().stream().map(GroupMember::getUserId).collect(Collectors.toSet());
        Specification<User> userSpecification = (root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get(User.Fields.id)).value(userIds);

        Map<String, User> userMap = userService.findAll(userSpecification, Pageable.unpaged()).getContent().stream()
                .collect(toMap(User::getId, Function.identity()));

        return page.map(groupMember ->
                GetGroupMemberCase.Response.from(groupMember, userMap.get(groupMember.getUserId()))
        );
    }

    public JoinGroupCase.Response joinGroup(String id, User operator) {
        GroupMember member = groupService.addNormalMember(id, operator);
        return JoinGroupCase.Response.from(member);
    }

    public void exitGroup(String id, User operator) {
        groupService.deleteNormalMember(id, operator);
    }

    public UpdateGroupMemberCase.Response updateMember(String id, String userId,
                                                       UpdateGroupMemberCase.Request request,
                                                       User operator) {
        GroupMember groupMember = groupService.changeMemberRole(id, userId, request.getRole(), operator);

        return UpdateGroupMemberCase.Response.from(groupMember);
    }

    public ChangeGroupOwnerCase.Response changeOwner(String id,
                                                     ChangeGroupOwnerCase.Request request,
                                                     User operator) {
        GroupMember groupMember = groupService.changeOwner(id, request.getUserId(), operator);
        return ChangeGroupOwnerCase.Response.from(groupMember);
    }

    public void removeMember(String id, String userId, User operator) {
        groupService.deleteMember(id, userId, operator);
    }
}
