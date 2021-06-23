package com.example.business.rest;

import com.example.TestBase;
import com.example.domain.group.model.Group;
import com.example.domain.group.model.GroupMember;
import com.example.domain.group.repository.GroupMemberRepository;
import com.example.domain.group.repository.GroupRepository;
import com.example.domain.group.service.GroupService;
import com.example.domain.user.model.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;

class GroupControllerTest extends TestBase {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private GroupService groupService;

    @Test
    void should_create_group() {
        User user = this.prepareUser("anyName", "anyEmail");
        String name = "name";
        String description = "description";

        Response response = givenWithAuthorize(user)
                .body(new HashMap<String, Object>() {
                    {
                        put("name", name);
                        put("description", description);
                    }
                })
                .when()
                .post("/groups");
        response.then().statusCode(201)
                .body("id", isA(String.class))
                .body("name", is(name))
                .body("description", is(description));

        Optional<Group> optionalGroup = groupRepository.findOne(Example.of(Group
                .builder()
                .createdBy(user.getId())
                .build()
        ));

        assertThat(optionalGroup.isPresent(), is(true));

        List<GroupMember> members = groupMemberRepository
                .findAll(Example.of(GroupMember.builder().groupId(optionalGroup.get().getId()).build()));
        assertThat(members.size(), is(1));
        assertThat(members.get(0).getUserId(), is(user.getId()));
        assertThat(members.get(0).getRole(), is(GroupMember.GroupMemberRole.OWNER));

    }

    @Test
    void should_get_all_groups_by_page() {
        User user = this.prepareUser("anyName", "anyEmail");
        Group group0 = groupService.create("name0", "anyDescription", user.getId());
        Group group1 = groupService.create("name1", "anyDescription", user.getId());
        groupService.create("name2", "anyDescription", user.getId());


        Response response = givenDefault()
                .param("sort", "createdAt")
                .param("sort", "name")
                .param("size", 2)
                .when()
                .get("/groups");

        response.then().statusCode(200)
                .body("content.size", is(2))
                .body("content.name", hasItems(group0.getName(), group1.getName()))
                .body("content.members[0]", hasSize(1));

    }

    @Test
    void should_get_all_mine_groups_by_page() {
        User user = this.prepareUser("anyName", "anyEmail");
        User otherUser = this.prepareUser("otherName", "otherEmail");
        Group group0 = groupService.create("name0", "anyDescription", user.getId());
        groupService.create("name1", "anyDescription", otherUser.getId());
        Group group2 = groupService.create("name2", "anyDescription", user.getId());


        Response response = givenWithAuthorize(user)
                .param("sort", "createdAt")
                .param("sort", "name")
                .param("size", 2)
                .when()
                .get("/groups/mine");

        response.then().statusCode(200)
                .body("content.size", is(2))
                .body("content.name", hasItems(group0.getName(), group2.getName()));
    }

    @Test
    void should_update_group() {
        User user = this.prepareUser("anyName", "anyEmail");
        Group group = groupService.create("name", "description", user.getId());

        String newName = "newName";
        String newDescription = "newDescription";

        Response response = givenWithAuthorize(user)
                .body(new HashMap<String, Object>() {
                    {
                        put("name", newName);
                        put("description", newDescription);
                    }
                })
                .when()
                .put("/groups/" + group.getId());
        response.then().statusCode(200)
                .body("id", isA(String.class))
                .body("name", is(newName))
                .body("description", is(newDescription));

        Group updatedGroup = groupRepository.findById(group.getId()).get();
        assertThat(updatedGroup.getName(), is(newName));
        assertThat(updatedGroup.getDescription(), is(newDescription));

    }

    @Test
    void should_join_group() {
        User creator = this.prepareUser("anyName", "anyEmail");
        Group group = groupService.create("name", "description", creator.getId());

        User user = this.prepareUser("otherName", "otherEmail");

        Response response = givenWithAuthorize(user)
                .when()
                .post("/groups/" + group.getId() + "/members");
        response.then().statusCode(201)
                .body("groupId", is(group.getId()))
                .body("userId", is(user.getId()))
                .body("role", is(GroupMember.GroupMemberRole.NORMAL.name()));

        Optional<GroupMember> optionalGroupMember = groupMemberRepository.findOne(Example.of(GroupMember.builder()
                .groupId(group.getId())
                .userId(user.getId())
                .build()));
        assertThat(optionalGroupMember.isPresent(), is(true));
        assertThat(optionalGroupMember.get().getRole(), is(GroupMember.GroupMemberRole.NORMAL));
    }

    @Test
    void should_exit_group() {
        User creator = this.prepareUser("anyName", "anyEmail");
        Group group = groupService.create("name", "description", creator.getId());

        User user = this.prepareUser("otherName", "otherEmail");

        Response response = givenWithAuthorize(user)
                .when()
                .delete("/groups/" + group.getId() + "/members");
        response.then().statusCode(200);

        Optional<GroupMember> optionalGroupMember = groupMemberRepository.findOne(Example.of(GroupMember.builder()
                .groupId(group.getId())
                .userId(user.getId())
                .build()));
        assertThat(optionalGroupMember.isPresent(), is(false));
    }

}
