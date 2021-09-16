package com.example.admin.rest;

import com.example.TestBase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.group.model.Group;
import com.example.domain.group.service.GroupService;
import com.example.domain.user.model.Operator;
import com.example.domain.user.model.User;
import com.example.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

class GroupManagementControllerTest extends TestBase {

    private Authorize authorize;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        authorize = this.prepareAuthorize();
    }

    @Test
    void should_get_question_list() {
        User user1 = userService.create("anyName1", "anyEmail1", "anyPassword");
        User user2 = userService.create("anyName2", "anyEmail2", "anyPassword");

        Operator operator1 = getOperator(user1);
        Operator operator2 = getOperator(user2);

        Group group1 = groupService.create("anyGroupName1", "anyDescription", operator1);
        Group group2 = groupService.create("anyGroupName2", "anyDescription", operator1);
        Group group3 = groupService.create("anyGroupName3", "anyDescription", operator2);


        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .param("sort", "name")
                .when()
                .get("/management/groups")
                .then().statusCode(200)
                .body("content.size", is(3))
                .body("content[0].id", is(group1.getId()))
                .body("content[0].name", is(group1.getName()))
                .body("content[0].description", is(group1.getDescription()))
                .body("content[1].id", is(group2.getId()))
                .body("content[1].name", is(group2.getName()))
                .body("content[1].description", is(group2.getDescription()))
                .body("content[2].id", is(group3.getId()))
                .body("content[2].name", is(group3.getName()))
                .body("content[2].description", is(group3.getDescription()))
        ;

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .param("keyword", "name1")
                .param("sort", "name")
                .when()
                .get("/management/groups")
                .then().statusCode(200)
                .body("content.size", is(1))
                .body("content[0].id", is(group1.getId()))
                .body("content[0].name", is(group1.getName()))
                .body("content[0].description", is(group1.getDescription()))
        ;


    }

    private Operator getOperator(User user) {
        return Operator.builder().userId(user.getId()).role(user.getRole()).build();
    }
}
