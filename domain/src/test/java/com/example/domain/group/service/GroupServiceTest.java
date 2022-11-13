package com.example.domain.group.service;

import com.example.domain.common.BaseException;
import com.example.domain.group.exception.GroupException;
import com.example.domain.group.model.Group;
import com.example.domain.group.model.GroupMember;
import com.example.domain.group.repository.GroupMemberRepository;
import com.example.domain.group.repository.GroupRepository;
import com.example.domain.iam.user.model.Operator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMemberRepository groupMemberRepository;

    @InjectMocks
    private GroupService groupService;

    @Test
    void should_throw_not_found_when_group_does_not_exist() {
        // given
        Mockito.when(groupRepository.findById(eq("test-group-id"))).thenReturn(Optional.empty());
        // when
        BaseException exception = assertThrows(GroupException.class, () -> {
            //when
            groupService.get("test-group-id");
        });

        assertEquals("group_not_found", exception.getMessage());
        assertEquals(BaseException.Type.NOT_FOUND, exception.getType());
    }


    @Test
    void should_update_failed_if_no_permission() {
        Group group = Group.build("any", "any", "any");
        group.setMembers(Collections.emptyList());
        Mockito.when(groupRepository.findById(eq(group.getId()))).thenReturn(Optional.of(group));

        BaseException exception = assertThrows(GroupException.class, () -> {
            //when
            groupService.update(
                    group.getId(), "test new group", "test new group description",
                    Operator.builder().userId("test-user-id").build()
            );
        });

        assertEquals("group_operation_forbidden", exception.getMessage());
        assertEquals(BaseException.Type.FORBIDDEN, exception.getType());
    }

    @Test
    void should_add_normal_member_failed_if_already_exist() {
        // given
        Group group = Group.build("any", "any", "any");
        Mockito.when(groupRepository.findById(eq(group.getId()))).thenReturn(Optional.of(group));
        Mockito.when(groupMemberRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(
                        GroupMember.build(group.getId(), "test-user-id", GroupMember.Role.OWNER, "test-user-id")));

        BaseException exception = assertThrows(GroupException.class, () -> {
            //when
            groupService.addNormalMember(group.getId(), Operator.builder().userId("test-user-id").build());
        });

        assertEquals("group_member_conflict", exception.getMessage());
        assertEquals(BaseException.Type.CONFLICT, exception.getType());
    }

    @Test
    void should_delete_normal_member_failed_if_role_is_owner() {
        // given
        String groupId = "test-group-id";
        String userId = "test-user-id";

        GroupMember groupMember = GroupMember.build(groupId, userId, GroupMember.Role.OWNER, userId);
        Mockito.when(groupMemberRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(groupMember));

        // Then
        BaseException exception = assertThrows(GroupException.class, () -> {
            //when
            groupService.deleteNormalMember(groupId, Operator.builder().userId(userId).build());
        });

        assertEquals("group_owner_can_not_exit", exception.getMessage());
        assertEquals(BaseException.Type.BAD_REQUEST, exception.getType());
    }
}
