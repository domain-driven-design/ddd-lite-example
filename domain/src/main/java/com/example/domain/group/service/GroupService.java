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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
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
        Group group = Group.builder()
                .name(name)
                .description(description)
                .createdBy(operator.getUserId())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        Group createdGroup = groupRepository.save(group);

        GroupMember owner = GroupMember.builder()
                .role(GroupMember.Role.OWNER)
                .groupId(createdGroup.getId())
                .userId(operator.getUserId())
                .createdBy(operator.getUserId())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        groupMemberRepository.save(owner);

        createdGroup.setMembers(Collections.singletonList(owner));

        return createdGroup;
    }

    public Group create(String name, String description, String ownerId, Operator operator) {
        Group group = Group.builder()
                .name(name)
                .description(description)
                .createdBy(operator.getUserId())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        Group createdGroup = groupRepository.save(group);

        GroupMember owner = GroupMember.builder()
                .role(GroupMember.Role.OWNER)
                .groupId(createdGroup.getId())
                .userId(ownerId)
                .createdBy(operator.getUserId())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
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
        group.setUpdatedAt(Instant.now());

        return groupRepository.save(group);
    }

    private Optional<GroupMember> findMember(String id, String userId) {
        return groupMemberRepository.findOne(Example.of(GroupMember.builder()
                .groupId(id)
                .userId(userId)
                .build()));
    }

    private GroupMember _getMember(String id, String userId) {
        return groupMemberRepository.findOne(Example.of(GroupMember.builder()
                .groupId(id)
                .userId(userId)
                .build()))
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
        GroupMember member = groupMemberRepository.findOne(Example.of(GroupMember.builder()
                .groupId(id)
                .userId(operator.getUserId())
                .build()))
                .orElseThrow(GroupException::memberNotFound);

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

        Optional<GroupMember> optionalGroupMember = findMember(id, operator.getUserId());
        if (optionalGroupMember.isPresent()) {
            throw GroupException.memberConflict();
        }

        GroupMember member = GroupMember.builder()
                .groupId(id)
                .userId(operator.getUserId())
                .role(GroupMember.Role.NORMAL)
                .createdBy(operator.getUserId())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();


        return groupMemberRepository.save(member);
    }

    public void deleteNormalMember(String id, Operator operator) {
        Optional<GroupMember> optionalGroupMember = findMember(id, operator.getUserId());
        if (!optionalGroupMember.isPresent()) {
            return;
        }

        GroupMember groupMember = optionalGroupMember.get();

        if (groupMember.getRole().equals(GroupMember.Role.OWNER)) {
            throw GroupException.ownerCanNotExit();
        }

        groupMemberRepository.delete(groupMember);

    }

    private GroupMember checkMemberRole(String id, String userId, GroupMember.Role role) {
        Optional<GroupMember> optionalOperator = findMember(id, userId);

        if (!optionalOperator.isPresent() || optionalOperator.get().getRole().compareTo(role) < 0) {
            throw GroupException.forbidden();
        }

        return optionalOperator.get();
    }

    public GroupMember changeMemberRole(String id, String userId,
                                        GroupMember.Role role, Operator operator) {
        checkMemberRole(id, operator.getUserId(), GroupMember.Role.OWNER);

        GroupMember groupMember = _getMember(id, userId);

        if (role.equals(GroupMember.Role.OWNER) ||
                groupMember.getRole().equals(GroupMember.Role.OWNER)) {
            throw GroupException.ownerCanNotChange();
        }

        groupMember.setRole(role);
        groupMember.setUpdatedAt(Instant.now());

        return groupMemberRepository.save(groupMember);
    }

    @Transactional
    public GroupMember changeOwner(String id, String userId, Operator operator) {
        GroupMember owner = checkMemberRole(id, operator.getUserId(), GroupMember.Role.OWNER);

        GroupMember groupMember = _getMember(id, userId);

        owner.setRole(GroupMember.Role.ADMIN);
        owner.setUpdatedAt(Instant.now());
        groupMemberRepository.save(owner);

        groupMember.setRole(GroupMember.Role.OWNER);
        groupMember.setUpdatedAt(Instant.now());
        return groupMemberRepository.save(groupMember);
    }

    public void deleteMember(String id, String userId, Operator operator) {
        GroupMember groupMemberOperator = checkMemberRole(id, operator.getUserId(), GroupMember.Role.ADMIN);

        GroupMember groupMember = _getMember(id, userId);

        if (groupMemberOperator.getRole().compareTo(groupMember.getRole()) <= 0) {
            throw GroupException.forbidden();
        }

        groupMemberRepository.delete(groupMember);
    }
}
