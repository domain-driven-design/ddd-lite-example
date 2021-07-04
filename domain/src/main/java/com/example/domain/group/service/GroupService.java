package com.example.domain.group.service;

import com.example.domain.group.exception.GroupException;
import com.example.domain.group.model.Group;
import com.example.domain.group.model.GroupMember;
import com.example.domain.group.repository.GroupMemberRepository;
import com.example.domain.group.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
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

    // TODO 权限分级check
    private void checkOwner(Group group, String operatorId) {
        boolean isOwner = group.getMembers().stream()
                .anyMatch(groupMember -> groupMember.getUserId().equals(operatorId)
                        && groupMember.getRole().equals(GroupMember.GroupMemberRole.OWNER));

        if (!isOwner) {
            throw GroupException.forbidden();
        }

    }

    public Group create(String name, String description, String operatorId) {
        Group group = Group.builder()
                .name(name)
                .description(description)
                .createdBy(operatorId)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        Group createdGroup = groupRepository.save(group);

        GroupMember groupMember = GroupMember.builder()
                .role(GroupMember.GroupMemberRole.OWNER)
                .groupId(createdGroup.getId())
                .userId(operatorId)
                .createdBy(operatorId)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        groupMemberRepository.save(groupMember);

        return createdGroup;
    }

    public Group update(String id, String name, String description, String operatorId) {
        Group group = _get(id);

        checkOwner(group, operatorId);

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

    private GroupMember getMember(String id, String memberId) {
        return groupMemberRepository
                .findOne(Example.of(GroupMember.builder().groupId(id).id(memberId).build()))
                .orElseThrow(GroupException::memberNotFound);
    }

    public GroupMember addNormalMember(String id, String operatorId) {
        Group group = _get(id);

        // TODO 根据以后的业务规则调整，被管理员移除后不得加入

        Optional<GroupMember> optionalGroupMember = findMember(id, operatorId);
        if (optionalGroupMember.isPresent()) {
            throw GroupException.memberConflict();
        }

        GroupMember member = GroupMember.builder()
                .groupId(id)
                .userId(operatorId)
                .role(GroupMember.GroupMemberRole.NORMAL)
                .createdBy(operatorId)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();


        return groupMemberRepository.save(member);
    }

    public void deleteNormalMember(String id, String operatorId) {
        // TODO check operator
        Optional<GroupMember> optionalGroupMember = findMember(id, operatorId);
        if (!optionalGroupMember.isPresent()) {
            return;
        }

        GroupMember groupMember = optionalGroupMember.get();

        if (groupMember.getRole().equals(GroupMember.GroupMemberRole.OWNER)) {
            throw GroupException.ownerCanNotExit();
        }

        groupMemberRepository.delete(groupMember);

    }

    private GroupMember checkMemberRole(String id, String userId, GroupMember.GroupMemberRole role) {
        Optional<GroupMember> optionalOperator = findMember(id, userId);

        if (!optionalOperator.isPresent() || optionalOperator.get().getRole().compareTo(role) < 0) {
            throw GroupException.forbidden();
        }

        return optionalOperator.get();
    }

    public GroupMember changeMemberRole(String id, String memberId,
                                        GroupMember.GroupMemberRole role, String operatorId) {
        checkMemberRole(id, operatorId, GroupMember.GroupMemberRole.OWNER);

        GroupMember groupMember = getMember(id, memberId);

        if (role.equals(GroupMember.GroupMemberRole.OWNER) ||
                groupMember.getRole().equals(GroupMember.GroupMemberRole.OWNER)) {
            throw GroupException.ownerCanNotChange();
        }

        groupMember.setRole(role);
        groupMember.setUpdatedAt(Instant.now());

        return groupMemberRepository.save(groupMember);
    }

    @Transactional
    public GroupMember changeOwner(String id, String memberId, String operatorId) {
        GroupMember owner = checkMemberRole(id, operatorId, GroupMember.GroupMemberRole.OWNER);

        GroupMember groupMember = getMember(id, memberId);

        owner.setRole(GroupMember.GroupMemberRole.ADMIN);
        owner.setUpdatedAt(Instant.now());
        groupMemberRepository.save(owner);

        groupMember.setRole(GroupMember.GroupMemberRole.OWNER);
        groupMember.setUpdatedAt(Instant.now());
        return groupMemberRepository.save(groupMember);
    }

    public void deleteMember(String id, String memberId, String operatorId) {
        GroupMember operator = checkMemberRole(id, operatorId, GroupMember.GroupMemberRole.ADMIN);

        GroupMember groupMember = getMember(id, memberId);

        if (operator.getRole().compareTo(groupMember.getRole()) <= 0) {
            throw GroupException.forbidden();
        }

        groupMemberRepository.delete(groupMember);
    }
}
