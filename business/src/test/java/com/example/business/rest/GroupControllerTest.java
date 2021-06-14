package com.example.business.rest;

import com.example.TestBase;
import com.example.domain.group.model.Group;
import com.example.domain.group.model.GroupMember;
import com.example.domain.group.repository.GroupMemberRepository;
import com.example.domain.group.repository.GroupRepository;
import com.example.domain.user.model.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;

class GroupControllerTest extends TestBase {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;

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

        List<GroupMember> members = groupMemberRepository.findAll(Example.of(GroupMember.builder().groupId(optionalGroup.get().getId()).build()));
        assertThat(members.size(), is(1));
        assertThat(members.get(0).getUserId(), is(user.getId()));
        assertThat(members.get(0).getRole(), is(GroupMember.GroupMemberRole.OWNER));

    }


}
