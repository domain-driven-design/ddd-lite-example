package com.example.domain.group.service;

import com.example.domain.group.exception.GroupException;
import com.example.domain.group.model.Group;
import com.example.domain.group.model.GroupMember;
import com.example.domain.group.model.GroupOperator;
import com.example.domain.group.repository.GroupMemberRepository;
import com.example.domain.group.repository.GroupRepository;
import com.example.domain.user.model.Operator;
import com.example.domain.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;

    public Group get(String id) {
        return _get(id);
    }

    private Group _get(String id) {
        return groupRepository.findById(id).orElseThrow(GroupException::notFound);
    }

    public Page<Group> findAll(Specification<Group> spec, Pageable pageable) {
        return groupRepository.findAll(spec, pageable);
    }

    public List<Group> findAll(Specification<Group> spec) {
        return groupRepository.findAll(spec);
    }

    public Group create(String name, String description, Operator operator) {
        Group group = Group.build(name, description, operator.getUserId());

        Group createdGroup = groupRepository.save(group);

        GroupMember owner =
                GroupMember.build(group.getId(), operator.getUserId(), GroupMember.Role.OWNER, operator.getUserId());
        groupMemberRepository.save(owner);

        createdGroup.setMembers(Collections.singletonList(owner));

        return createdGroup;
    }

    public Group create(String name, String description, String ownerId, Operator operator) {
        Group group = Group.build(name, description, operator.getUserId());

        Group createdGroup = groupRepository.save(group);

        GroupMember owner = GroupMember.build(group.getId(), ownerId, GroupMember.Role.OWNER, operator.getUserId());
        groupMemberRepository.save(owner);

        createdGroup.setMembers(Collections.singletonList(owner));

        return createdGroup;
    }

    public Group update(String id, String name, String description, Operator operator) {
        Group group = _get(id);

        boolean isOwner = group.getMembers().stream()
                .anyMatch(groupMember -> groupMember.getUserId().equals(operator.getUserId())
                        && groupMember.getRole().equals(GroupMember.Role.OWNER));

        if (!isOwner) {
            throw GroupException.forbidden();
        }

        group.setName(name);
        group.setDescription(description);

        return groupRepository.save(group);
    }

    private Optional<GroupMember> _findMember(String id, String userId) {
        Specification<GroupMember> specification = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get(GroupMember.Fields.groupId), id),
                criteriaBuilder.equal(root.get(GroupMember.Fields.userId), userId)
        );
        return groupMemberRepository.findOne(specification);
    }

    private GroupMember _getMember(String id, String userId) {
        return _findMember(id, userId)
                .orElseThrow(GroupException::memberNotFound);
    }

    public GroupOperator getOperator(String id, Operator operator) {
        if (operator.getRole().equals(User.Role.ADMIN)) {
            return GroupOperator.builder()
                    .groupId(id)
                    .userId(operator.getUserId())
                    .role(GroupMember.Role.OWNER)
                    .build();
        }

        if (id.equals(Group.DEFAULT)) {
            return GroupOperator.builder()
                    .groupId(id)
                    .userId(operator.getUserId())
                    .role(GroupMember.Role.NORMAL)
                    .build();
        }
        GroupMember member = _getMember(id, operator.getUserId());

        return GroupOperator.builder()
                .groupId(id)
                .userId(operator.getUserId())
                .role(member.getRole())
                .build();
    }

    public Page<GroupMember> findAllMembers(Specification<GroupMember> specification, Pageable pageable) {
        return groupMemberRepository.findAll(specification, pageable);
    }

    public GroupMember addNormalMember(String id, Operator operator) {
        Group group = _get(id);

        Optional<GroupMember> optionalGroupMember = _findMember(id, operator.getUserId());
        if (optionalGroupMember.isPresent()) {
            throw GroupException.memberConflict();
        }

        GroupMember member =
                GroupMember.build(group.getId(), operator.getUserId(), GroupMember.Role.NORMAL, operator.getUserId());

        return groupMemberRepository.save(member);
    }

    public void deleteNormalMember(String id, Operator operator) {
        Optional<GroupMember> optionalGroupMember = _findMember(id, operator.getUserId());
        if (!optionalGroupMember.isPresent()) {
            return;
        }

        GroupMember groupMember = optionalGroupMember.get();

        if (groupMember.getRole().equals(GroupMember.Role.OWNER)) {
            throw GroupException.ownerCanNotExit();
        }

        groupMemberRepository.delete(groupMember);

    }

    public GroupMember changeMemberRole(String id, String userId,
                                        GroupMember.Role role, GroupOperator operator) {
        if (!operator.getRole().equals(GroupMember.Role.OWNER)) {
            throw GroupException.forbidden();
        }

        GroupMember groupMember = _getMember(id, userId);

        if (role.equals(GroupMember.Role.OWNER) ||
                groupMember.getRole().equals(GroupMember.Role.OWNER)) {
            throw GroupException.ownerCanNotChange();
        }

        groupMember.setRole(role);

        return groupMemberRepository.save(groupMember);
    }

    @Transactional
    public GroupMember changeOwner(String id, String userId, GroupOperator operator) {
        if (!operator.getRole().equals(GroupMember.Role.OWNER)) {
            throw GroupException.forbidden();
        }

        GroupMember groupOwner = _getMember(id, operator.getUserId());
        GroupMember groupMember = _getMember(id, userId);

        groupOwner.setRole(GroupMember.Role.ADMIN);
        groupMemberRepository.save(groupOwner);

        groupMember.setRole(GroupMember.Role.OWNER);
        return groupMemberRepository.save(groupMember);
    }

    public void deleteMember(String id, String userId, GroupOperator operator) {
        if (operator.getRole().compareTo(GroupMember.Role.ADMIN) < 0) {
            throw GroupException.forbidden();
        }

        GroupMember groupMember = _getMember(id, userId);

        if (operator.getRole().compareTo(groupMember.getRole()) <= 0) {
            throw GroupException.forbidden();
        }

        groupMemberRepository.delete(groupMember);
    }
}
