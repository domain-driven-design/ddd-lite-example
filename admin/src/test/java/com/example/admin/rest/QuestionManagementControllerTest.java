package com.example.admin.rest;

import com.example.TestBase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.group.model.Group;
import com.example.domain.group.model.GroupOperator;
import com.example.domain.group.service.GroupService;
import com.example.domain.question.model.Question;
import com.example.domain.question.service.QuestionService;
import com.example.domain.user.model.User;
import com.example.domain.user.service.UserService;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;

class QuestionManagementControllerTest extends TestBase {

    private Authorize authorize;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

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

        GroupOperator defaultOperator1 = groupService.getOperator(Group.DEFAULT, user1.getId());
        GroupOperator defaultOperator2 = groupService.getOperator(Group.DEFAULT, user2.getId());

        Question question1 = questionService.create("anyTitle1", "anyDescription", defaultOperator1);
        Question question2 = questionService.create("anyTitle2", "anyDescription", defaultOperator2);

        Response response0 = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .param("keyword", "any")
                .when()
                .get("/management/questions");
        response0.then().statusCode(200)
                .body("content.size", is(2))
                .body("content.id", hasItems(question1.getId(), question2.getId()))
                .body("content.title", hasItems(question1.getTitle(), question2.getTitle()))
                .body("content.description", hasItems(question1.getDescription(), question2.getDescription()))
                .body("content.group.id", hasItems(Group.DEFAULT))
                .body("content.creator.id", hasItems(user1.getId(), user2.getId()))
                .body("content.creator.name", hasItems(user1.getName(), user2.getName()));


        Response response1 = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .param("keyword", "Title1")
                .when()
                .get("/management/questions");
        response1.then().statusCode(200)
                .body("content.size", is(1))
                .body("content.id", hasItems(question1.getId()));
    }
}
