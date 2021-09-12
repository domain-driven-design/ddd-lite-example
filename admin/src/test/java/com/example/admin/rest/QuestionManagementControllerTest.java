package com.example.admin.rest;

import com.example.TestBase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.group.model.Group;
import com.example.domain.group.model.GroupOperator;
import com.example.domain.group.service.GroupService;
import com.example.domain.question.model.Answer;
import com.example.domain.question.model.Question;
import com.example.domain.question.repository.AnswerRepository;
import com.example.domain.question.repository.QuestionRepository;
import com.example.domain.question.service.QuestionService;
import com.example.domain.user.model.Operator;
import com.example.domain.user.model.User;
import com.example.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

class QuestionManagementControllerTest extends TestBase {

    private Authorize authorize;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

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

        GroupOperator defaultOperator1 = groupService.getOperator(Group.DEFAULT, operator1);
        GroupOperator defaultOperator2 = groupService.getOperator(Group.DEFAULT, operator2);

        Question question1 = questionService.create("anyTitle1", "anyDescription", defaultOperator1);
        Question question2 = questionService.create("anyTitle2", "anyDescription", defaultOperator2);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .param("keyword", "any")
                .when()
                .get("/management/questions")
                .then().statusCode(200)
                .body("content.size", is(2))
                .body("content.id", hasItems(question1.getId(), question2.getId()))
                .body("content.title", hasItems(question1.getTitle(), question2.getTitle()))
                .body("content.description", hasItems(question1.getDescription(), question2.getDescription()))
                .body("content.group.id", hasItems(Group.DEFAULT))
                .body("content.creator.id", hasItems(user1.getId(), user2.getId()))
                .body("content.creator.name", hasItems(user1.getName(), user2.getName()));


        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .param("keyword", "Title1")
                .when()
                .get("/management/questions")
                .then().statusCode(200)
                .body("content.size", is(1))
                .body("content.id", hasItems(question1.getId()));
    }

    @Test
    void should_update_question_status() {
        User user = userService.create("anyName1", "anyEmail1", "anyPassword");
        Operator operator = getOperator(user);
        Group group = groupService.create("anyGroupName", "", operator);
        GroupOperator groupOperator = groupService.getOperator(group.getId(), operator);

        Question question = questionService.create("anyTitle", "anyDescription", groupOperator);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .header("Group-ID", group.getId())
                .body(new HashMap<String, Object>() {
                    {
                        put("status", Question.Status.CLOSED);
                    }
                })
                .when()
                .put("/management/questions/" + question.getId() + "/status")
                .then().statusCode(200)
                .body("id", is(question.getId()))
                .body("status", is(Question.Status.CLOSED.name()));

        Question updatedQuestion = questionRepository.findById(question.getId()).get();
        assertThat(updatedQuestion.getStatus(), is(Question.Status.CLOSED));

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .header("Group-ID", group.getId())
                .body(new HashMap<String, Object>() {
                    {
                        put("status", Question.Status.OPENED);
                    }
                })
                .when()
                .put("/management/questions/" + question.getId() + "/status")
                .then().statusCode(200)
                .body("id", is(question.getId()))
                .body("status", is(Question.Status.OPENED.name()));

        updatedQuestion = questionRepository.findById(question.getId()).get();
        assertThat(updatedQuestion.getStatus(), is(Question.Status.OPENED));

    }

    @Test
    void should_delete_question() {
        User user = userService.create("anyName1", "anyEmail1", "anyPassword");
        Operator operator = getOperator(user);
        GroupOperator groupOperator = groupService.getOperator(Group.DEFAULT, operator);

        Question question = questionService.create("anyTitle", "anyDescription", groupOperator);
        String questionId = question.getId();

        questionService.addAnswer(questionId, "content0", groupOperator);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .header("Group-ID", Group.DEFAULT)
                .when()
                .delete("/management/questions/" + question.getId())
                .then().statusCode(200);

        assertThat(questionRepository.existsById(questionId), is(false));

        List<Answer> answers = answerRepository.findAll(Example.of(Answer.builder().questionId(questionId).build()));
        assertThat(answers, hasSize(0));
    }

    private Operator getOperator(User user) {
        return Operator.builder().userId(user.getId()).role(user.getRole()).build();
    }
}
