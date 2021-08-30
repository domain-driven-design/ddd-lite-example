package com.example.domain.group.service;

import com.example.domain.common.BaseException;
import com.example.domain.group.exception.GroupException;
import com.example.domain.group.model.Group;
import com.example.domain.group.model.GroupMember;
import com.example.domain.group.repository.GroupMemberRepository;
import com.example.domain.group.repository.GroupRepository;
import com.example.domain.user.model.Operator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

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
        Group group = Group.builder()
                .id("test-group-id").name("test group").description("test group description")
                .members(Collections.emptyList())
                .build();
        Mockito.when(groupRepository.findById(eq("test-group-id"))).thenReturn(Optional.of(group));

        BaseException exception = assertThrows(GroupException.class, () -> {
            //when
            groupService.update(
                    "test-group-id", "test new group", "test new group description",
                    Operator.builder().userId("test-user-id").build()
            );
        });

        assertEquals("group_operation_forbidden", exception.getMessage());
        assertEquals(BaseException.Type.FORBIDDEN, exception.getType());
    }

    @Test
    void should_add_normal_member_failed_if_already_exist() {
        // given
        Group group = Group.builder()
                .id("test-group-id").name("test group").description("test group description")
                .build();
        Mockito.when(groupRepository.findById(eq("test-group-id"))).thenReturn(Optional.of(group));
        Mockito.when(groupMemberRepository.findOne(any(Example.class)))
                .thenReturn(Optional.of(GroupMember.builder().build()));

        BaseException exception = assertThrows(GroupException.class, () -> {
            //when
            groupService.addNormalMember("test-group-id", Operator.builder().userId("test-user-id").build());
        });

        assertEquals("group_member_conflict", exception.getMessage());
        assertEquals(BaseException.Type.CONFLICT, exception.getType());
    }

    @Test
    void should_delete_normal_member_failed_if_role_is_owner() {
        // given
        String groupId = "test-group-id";
        String userId = "test-user-id";

        GroupMember groupMember = GroupMember.builder()
                .groupId(groupId)
                .userId(userId)
                .role(GroupMember.Role.OWNER)
                .build();
        Mockito.when(groupMemberRepository.findOne(any(Example.class)))
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
