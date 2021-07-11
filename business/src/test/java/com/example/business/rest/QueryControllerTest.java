package com.example.business.rest;

import com.example.TestBase;
import com.example.domain.group.model.Group;
import com.example.domain.group.service.GroupService;
import com.example.domain.question.model.Question;
import com.example.domain.question.service.QuestionService;
import com.example.domain.user.model.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.hasSize;

class QueryControllerTest extends TestBase {
    public static final String DEFAULT_PATH = "/query";

    @Autowired
    private QuestionService questionService;
    @Autowired
    private GroupService groupService;

    @Test
    void should_query_questions() {
        User user = this.prepareUser("anyName", "anyEmail");
        User otherUser = this.prepareUser("anyOtherName", "anyOtherEmail");
        Group group = groupService.create("anyName", "anyDescription", user.getId());

        Question question0 = questionService.create("title0", "anyDescription", Group.DEFAULT, user.getId());
        Question question1 = questionService.create("title1", "anyDescription", group.getId(), user.getId());
        Question question2 = questionService.create("title2", "anyDescription", Group.DEFAULT, user.getId());
        Question question3 = questionService.create("title3", "anyDescription", Group.DEFAULT, user.getId());

        questionService.addAnswer(question0.getId(), "anyContent", user.getId());
        questionService.addAnswer(question1.getId(), "anyContent", user.getId());
        questionService.addAnswer(question2.getId(), "anyContent", user.getId());
        questionService.addAnswer(question2.getId(), "anyContent", otherUser.getId());
        questionService.addAnswer(question3.getId(), "anyContent", otherUser.getId());

        Response response = givenWithAuthorize(user)
                .param("userId", user.getId())
                .when()
                .get(DEFAULT_PATH + "/answers");
        response.then().statusCode(200)
                .body("content", hasSize(3));
    }


}