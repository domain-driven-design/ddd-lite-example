package com.example.business.rest;

import com.example.TestBase;
import com.example.domain.group.model.Group;
import com.example.domain.group.model.GroupMember;
import com.example.domain.group.repository.GroupMemberRepository;
import com.example.domain.group.repository.GroupRepository;
import com.example.domain.group.service.GroupService;
import com.example.domain.user.model.Operator;
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
        assertThat(members.get(0).getRole(), is(GroupMember.Role.OWNER));

    }

    @Test
    void should_get_group_detail() {
        User user = this.prepareUser("anyName", "anyEmail");
        Operator operator = getOperator(user);
        Group group = groupService.create("name", "anyDescription", operator);


        Response response = givenDefault()
                .when()
                .get("/groups/" + group.getId());

        response.then().statusCode(200)
                .body("name", is(group.getName()))
                .body("description", is(group.getDescription()))
                .body("members", hasSize(1))
                .body("members[0].userId", is(user.getId()))
                .body("members[0].role", is(GroupMember.Role.OWNER.name()))
                .body("creator.id", is(user.getId()))
                .body("creator.name", is(user.getName()));
    }

    @Test
    void should_get_all_groups_by_page() {
        User user = this.prepareUser("anyName", "anyEmail");
        Operator operator = getOperator(user);

        Group group0 = groupService.create("name0", "anyDescription", operator);
        Group group1 = groupService.create("name1", "anyDescription", operator);
        groupService.create("name2", "anyDescription", operator);


        Response response = givenDefault()
                .param("sort", "createdAt")
                .param("sort", "name")
                .param("size", 2)
                .when()
                .get("/groups");

        response.then().statusCode(200)
                .body("content.size", is(2))
                .body("content.description", hasItems(group0.getDescription(), group1.getDescription()))
                .body("content.name", hasItems(group0.getName(), group1.getName()))
                .body("content.members[0]", hasSize(1));

    }

    @Test
    void should_get_all_mine_groups_by_page() {
        User user = this.prepareUser("anyName", "anyEmail");
        Operator operator = getOperator(user);
        User otherUser = this.prepareUser("otherName", "otherEmail");
        Operator otherOperator = getOperator(otherUser);

        Group group0 = groupService.create("name0", "anyDescription", operator);
        groupService.create("name1", "anyDescription", otherOperator);
        Group group2 = groupService.create("name2", "anyDescription", operator);


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
        Operator operator = getOperator(user);

        Group group = groupService.create("name", "description", operator);

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
    void should_get_group_members() {
        User creator = this.prepareUser("anyName", "anyEmail");
        Operator operator = getOperator(creator);

        Group group = groupService.create("name", "description", operator);

        User user1 = this.prepareUser("name1", "email1");
        Operator operator1 = getOperator(user1);
        User user2 = this.prepareUser("name2", "email2");
        Operator operator2 = getOperator(user2);
        User user3 = this.prepareUser("name3", "email3");
        Operator operator3 = getOperator(user3);

        groupService.addNormalMember(group.getId(), operator1);
        groupService.addNormalMember(group.getId(), operator2);
        groupService.addNormalMember(group.getId(), operator3);

        Response response = givenWithAuthorize(creator)
                .param("sort", "createdAt")
                .when()
                .get("/groups/" + group.getId() + "/members");

        response.then().statusCode(200)
                .body("content.size", is(4))
                .body("content.userId", hasItems(creator.getId(), user1.getId(), user2.getId(), user3.getId()))
                .body("content.name", hasItems(creator.getName(), user1.getName(), user2.getName(), user3.getName()))
                .body("content.role", hasItems(GroupMember.Role.NORMAL.name(), GroupMember.Role.OWNER.name()));
    }

    @Test
    void should_get_group_management_members() {
        User creator = this.prepareUser("anyName", "anyEmail");
        Operator operator = getOperator(creator);

        Group group = groupService.create("name", "description", operator);

        User user1 = this.prepareUser("name1", "email1");
        Operator operator1 = getOperator(user1);
        User user2 = this.prepareUser("name2", "email2");
        Operator operator2 = getOperator(user2);
        User user3 = this.prepareUser("name3", "email3");
        Operator operator3 = getOperator(user3);

        groupService.addNormalMember(group.getId(), operator1);
        groupService.addNormalMember(group.getId(), operator2);
        groupService.addNormalMember(group.getId(), operator3);

        Response response = givenWithAuthorize(creator)
                .param("sort", "createdAt")
                .when()
                .get("/groups/" + group.getId() + "/members/management");

        response.then().statusCode(200)
                .body("content.size", is(3))
                .body("content.userId", hasItems(user1.getId(), user2.getId(), user3.getId()))
                .body("content.name", hasItems(user1.getName(), user2.getName(), user3.getName()))
                .body("content.role", hasItems(GroupMember.Role.NORMAL.name()));
    }

    @Test
    void should_join_group() {
        User creator = this.prepareUser("anyName", "anyEmail");
        Operator operator = getOperator(creator);
        Group group = groupService.create("name", "description", operator);

        User user = this.prepareUser("otherName", "otherEmail");

        Response response = givenWithAuthorize(user)
                .when()
                .post("/groups/" + group.getId() + "/members/me");
        response.then().statusCode(201)
                .body("groupId", is(group.getId()))
                .body("userId", is(user.getId()))
                .body("role", is(GroupMember.Role.NORMAL.name()));

        Optional<GroupMember> optionalGroupMember = groupMemberRepository.findOne(Example.of(GroupMember.builder()
                .groupId(group.getId())
                .userId(user.getId())
                .build()));
        assertThat(optionalGroupMember.isPresent(), is(true));
        assertThat(optionalGroupMember.get().getRole(), is(GroupMember.Role.NORMAL));
    }

    @Test
    void should_exit_group() {
        User creator = this.prepareUser("anyName", "anyEmail");
        Operator operator = getOperator(creator);

        Group group = groupService.create("name", "description", operator);

        User user = this.prepareUser("otherName", "otherEmail");

        Response response = givenWithAuthorize(user)
                .when()
                .delete("/groups/" + group.getId() + "/members/me");
        response.then().statusCode(200);

        Optional<GroupMember> optionalGroupMember = groupMemberRepository.findOne(Example.of(GroupMember.builder()
                .groupId(group.getId())
                .userId(user.getId())
                .build()));
        assertThat(optionalGroupMember.isPresent(), is(false));
    }

    @Test
    void should_change_member_role() {
        User creator = this.prepareUser("anyName", "anyEmail");
        Operator operator = getOperator(creator);

        Group group = groupService.create("name", "description", operator);

        User user = this.prepareUser("otherName", "otherEmail");
        Operator otherOperator = getOperator(user);

        GroupMember groupMember = groupService.addNormalMember(group.getId(), otherOperator);

        Response response = givenWithAuthorize(creator)
                .body(new HashMap<String, Object>() {
                    {
                        put("role", GroupMember.Role.ADMIN);
                    }
                })                .when()
                .put("/groups/" + group.getId() + "/members/" + groupMember.getUserId());
        response.then().statusCode(200)
                .body("groupId", is(group.getId()))
                .body("userId", is(user.getId()))
                .body("role", is(GroupMember.Role.ADMIN.name()));

        Optional<GroupMember> optionalGroupMember = groupMemberRepository.findOne(Example.of(GroupMember.builder()
                .groupId(group.getId())
                .userId(user.getId())
                .build()));
        assertThat(optionalGroupMember.isPresent(), is(true));
        assertThat(optionalGroupMember.get().getRole(), is(GroupMember.Role.ADMIN));

        response = givenWithAuthorize(creator)
                .body(new HashMap<String, Object>() {
                    {
                        put("role", GroupMember.Role.NORMAL);
                    }
                })                .when()
                .put("/groups/" + group.getId() + "/members/" + groupMember.getUserId());
        response.then().statusCode(200)
                .body("groupId", is(group.getId()))
                .body("userId", is(user.getId()))
                .body("role", is(GroupMember.Role.NORMAL.name()));

        optionalGroupMember = groupMemberRepository.findOne(Example.of(GroupMember.builder()
                .groupId(group.getId())
                .userId(user.getId())
                .build()));
        assertThat(optionalGroupMember.isPresent(), is(true));
        assertThat(optionalGroupMember.get().getRole(), is(GroupMember.Role.NORMAL));
    }

    @Test
    void should_change_owner() {
        User creator = this.prepareUser("anyName", "anyEmail");
        Operator operator = getOperator(creator);

        Group group = groupService.create("name", "description", operator);

        User user = this.prepareUser("otherName", "otherEmail");
        Operator otherOperator = getOperator(user);

        GroupMember groupMember = groupService.addNormalMember(group.getId(), otherOperator);

        Response response = givenWithAuthorize(creator)
                .body(new HashMap<String, Object>() {
                    {
                        put("userId", groupMember.getUserId());
                    }
                })                .when()
                .put("/groups/" + group.getId() + "/owner");
        response.then().statusCode(200)
                .body("groupId", is(group.getId()))
                .body("userId", is(user.getId()))
                .body("role", is(GroupMember.Role.OWNER.name()));

        Optional<GroupMember> optionalGroupMember = groupMemberRepository.findOne(Example.of(GroupMember.builder()
                .groupId(group.getId())
                .userId(user.getId())
                .build()));
        assertThat(optionalGroupMember.isPresent(), is(true));
        assertThat(optionalGroupMember.get().getRole(), is(GroupMember.Role.OWNER));

        Optional<GroupMember> optionalCreator = groupMemberRepository.findOne(Example.of(GroupMember.builder()
                .groupId(group.getId())
                .userId(creator.getId())
                .build()));
        assertThat(optionalCreator.isPresent(), is(true));
        assertThat(optionalCreator.get().getRole(), is(GroupMember.Role.ADMIN));

    }

    @Test
    void should_remove_member() {
        User creator = this.prepareUser("anyName", "anyEmail");
        Operator operator = getOperator(creator);

        Group group = groupService.create("name", "description", operator);

        User user = this.prepareUser("otherName", "otherEmail");
        Operator otherOperator = getOperator(user);
        GroupMember groupMember = groupService.addNormalMember(group.getId(), otherOperator);

        Response response = givenWithAuthorize(creator)
                .when()
                .delete("/groups/" + group.getId() + "/members/" + groupMember.getUserId());
        response.then().statusCode(200);

        Optional<GroupMember> optionalGroupMember = groupMemberRepository.findOne(Example.of(GroupMember.builder()
                .groupId(group.getId())
                .userId(user.getId())
                .build()));
        assertThat(optionalGroupMember.isPresent(), is(false));
    }

    private Operator getOperator(User user) {
        return Operator.builder().userId(user.getId()).role(user.getRole()).build();
    }
}
