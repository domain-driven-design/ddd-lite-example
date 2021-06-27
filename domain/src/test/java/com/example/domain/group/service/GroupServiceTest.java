package com.example.domain.group.service;

import com.example.domain.common.BaseException;
import com.example.domain.group.exception.GroupException;
import com.example.domain.group.model.Group;
import com.example.domain.group.model.GroupMember;
import com.example.domain.group.repository.GroupMemberRepository;
import com.example.domain.group.repository.GroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
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
    void should_get_group_when_group_exists() {
        // given
        Group group = Group.builder()
                .id("test-group-id").name("test group").description("test group description")
                .build();
        Mockito.when(groupRepository.findById(eq("test-group-id"))).thenReturn(Optional.of(group));

        // when
        Group groupFromService = groupService.get("test-group-id");

        // then
        assertEquals("test group", groupFromService.getName());
        assertEquals("test group description", groupFromService.getDescription());
    }

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
    void should_create_group() {
        Mockito.when(groupRepository.save(ArgumentMatchers.any())).thenReturn(
                Group.builder().id("test-group-id").build()
        );
        groupService.create(
                "test group name", "test group description", "test-user-id"
        );
        Mockito.verify(groupRepository).save(argThat((group) -> {
            assertEquals("test group name", group.getName());
            assertEquals("test group description", group.getDescription());
            assertEquals("test-user-id", group.getCreatedBy());
            assertThat(group.getCreatedAt(), isA(Instant.class));
            assertThat(group.getUpdatedAt(), isA(Instant.class));
            return true;
        }));

        Mockito.verify(groupMemberRepository).save(argThat((groupMember) -> {
            assertEquals("test-user-id", groupMember.getUserId());
            assertEquals("test-user-id", groupMember.getCreatedBy());
            assertEquals(GroupMember.GroupMemberRole.OWNER, groupMember.getRole());
            assertThat(groupMember.getCreatedAt(), isA(Instant.class));
            assertThat(groupMember.getUpdatedAt(), isA(Instant.class));
            return true;
        }));
    }


    @Test
    void should_update_group_success() {
        GroupMember groupMember = GroupMember.builder().groupId("test-group-id").role(GroupMember.GroupMemberRole.OWNER)
                .userId("test-user-id").build();
        Group group = Group.builder()
                .id("test-group-id").name("test group").description("test group description")
                .members(Collections.singletonList(groupMember))
                .build();
        Mockito.when(groupRepository.findById(eq("test-group-id"))).thenReturn(Optional.of(group));

        //when
        groupService.update(
                "test-group-id", "test new group", "test new group description",
                "test-user-id"
        );

        Mockito.verify(groupRepository).save(argThat((argument) -> {
            assertEquals("test new group", argument.getName());
            assertEquals("test new group description", argument.getDescription());
            return true;
        }));
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
                    "test-user-id"
            );
        });

        assertEquals("group_operation_forbidden", exception.getMessage());
        assertEquals(BaseException.Type.FORBIDDEN, exception.getType());
    }

    @Test
    void should_add_normal_member_success() {
        // given
        Group group = Group.builder()
                .id("test-group-id").name("test group").description("test group description")
                .build();
        Mockito.when(groupRepository.findById(eq("test-group-id"))).thenReturn(Optional.of(group));

        //when
        groupService.addNormalMember("test-group-id", "test-user");

        // Then
        Mockito.verify(groupMemberRepository).save(argThat((argument) -> {
            assertEquals("test-group-id", argument.getGroupId());
            assertEquals("test-user", argument.getUserId());
            assertEquals(GroupMember.GroupMemberRole.NORMAL, argument.getRole());
            return true;
        }));
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
            groupService.addNormalMember("test-group-id", "test-user");
        });

        assertEquals("group_member_conflict", exception.getMessage());
        assertEquals(BaseException.Type.CONFLICT, exception.getType());
    }

    @Test
    void should_delete_normal_member_success() {
        // given
        String groupId = "test-group-id";
        String userId = "test-user-id";

        GroupMember groupMember = GroupMember.builder()
                .groupId(groupId)
                .userId(userId)
                .role(GroupMember.GroupMemberRole.NORMAL)
                .build();
        Mockito.when(groupMemberRepository.findOne(any(Example.class)))
                .thenReturn(Optional.of(groupMember))
                .thenReturn(Optional.empty());

        //when
        groupService.deleteNormalMember(groupId, userId);
        groupService.deleteNormalMember(groupId, userId);

        // Then
        Mockito.verify(groupMemberRepository).delete(argThat((argument) -> {
            assertEquals(groupId, argument.getGroupId());
            assertEquals(userId, argument.getUserId());
            assertEquals(GroupMember.GroupMemberRole.NORMAL, argument.getRole());
            return true;
        }));
    }

    @Test
    void should_delete_normal_member_failed_if_role_is_owner() {
        // given
        String groupId = "test-group-id";
        String userId = "test-user-id";

        GroupMember groupMember = GroupMember.builder()
                .groupId(groupId)
                .userId(userId)
                .role(GroupMember.GroupMemberRole.OWNER)
                .build();
        Mockito.when(groupMemberRepository.findOne(any(Example.class)))
                .thenReturn(Optional.of(groupMember));

        // Then
        BaseException exception = assertThrows(GroupException.class, () -> {
            //when
            groupService.deleteNormalMember(groupId, userId);
        });

        assertEquals("group_owner_can_not_exit", exception.getMessage());
        assertEquals(BaseException.Type.BAD_REQUEST, exception.getType());
    }
}
