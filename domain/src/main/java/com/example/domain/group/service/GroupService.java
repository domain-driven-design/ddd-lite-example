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

import java.time.Instant;
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

        // TODO 业务规则？业务流程编排？
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

    public void delete(String id, String operatorId) {
        Optional<Group> optionalGroup = groupRepository.findById(id);
        if (!optionalGroup.isPresent()) {
            return;
        }

        Group group = optionalGroup.get();

        checkOwner(group, operatorId);

        List<GroupMember> members = groupMemberRepository.findAll(Example.of(GroupMember.builder().groupId(id).build()));
        groupMemberRepository.deleteAll(members);
        groupMemberRepository.deleteById(id);
    }

    public GroupMember addMember(String id, String operatorId) {
        Group group = _get(id);

        // TODO 根据以后的业务规则调整，被管理员移除后不得加入

        // TODO 业务确认：一个group，一个user只能有一个member（）
        boolean alreadyMember = groupMemberRepository.exists(Example.of(GroupMember.builder()
                .groupId(id)
                .userId(operatorId)
                .build()));
        if (alreadyMember) {
            throw GroupException.memberConflict();
        }

        GroupMember member = GroupMember.builder()
                .groupId(id)
                .userId(operatorId)
                .role(GroupMember.GroupMemberRole.MEMBER)
                .createdBy(operatorId)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();


        return groupMemberRepository.save(member);
    }

    public void deleteMember(String id, String operatorId) {
        Optional<GroupMember> optionalGroupMember = groupMemberRepository.findOne(Example.of(GroupMember.builder()
                .groupId(id)
                .userId(operatorId)
                .build()));
        if (!optionalGroupMember.isPresent()) {
            return;
        }

        GroupMember groupMember = optionalGroupMember.get();

        if (groupMember.getRole().equals(GroupMember.GroupMemberRole.OWNER)) {
            throw GroupException.ownerCanNotExit();
        }

        groupMemberRepository.delete(groupMember);

    }
}
