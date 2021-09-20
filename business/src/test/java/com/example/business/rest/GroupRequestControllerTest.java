package com.example.business.rest;

import com.example.TestBase;
import com.example.domain.group.model.GroupRequest;
import com.example.domain.group.repository.GroupRequestRepository;
import com.example.domain.user.model.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.HashMap;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;

class GroupRequestControllerTest extends TestBase {
    @Autowired
    private GroupRequestRepository groupRequestRepository;

    @Test
    void should_create_group_request() {
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
                .post("/group-requests");
        response.then().statusCode(201)
                .body("id", isA(String.class))
                .body("name", is(name))
                .body("description", is(description));

        Optional<GroupRequest> optionalGroup = groupRequestRepository.findOne(Example.of(GroupRequest
                .builder()
                .createdBy(user.getId())
                .build()
        ));

        assertThat(optionalGroup.isPresent(), is(true));

        GroupRequest groupRequest = optionalGroup.get();
        assertThat(groupRequest.getName(), is(name));
        assertThat(groupRequest.getDescription(), is(description));
    }
}
