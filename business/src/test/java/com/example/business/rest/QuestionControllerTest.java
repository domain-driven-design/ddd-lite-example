package com.example.business.rest;

import com.example.TestBase;
import com.example.domain.question.model.Answer;
import com.example.domain.question.model.Question;
import com.example.domain.question.repository.AnswerRepository;
import com.example.domain.question.repository.QuestionRepository;
import com.example.domain.question.service.QuestionService;
import com.example.domain.user.model.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.HashMap;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;

class QuestionControllerTest extends TestBase {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void should_create_question() {
        User user = this.prepareUser("anyName", "anyEmail");
        String title = "title";
        String description = "description";

        Response response = givenWithAuthorize(user)
                .body(new HashMap<String, Object>() {
                    {
                        put("title", title);
                        put("description", description);
                    }
                })
                .when()
                .post("/questions");
        response.then().statusCode(201)
                .body("id", isA(String.class))
                .body("title", is(title))
                .body("description", is(description));

        Optional<Question> questionOptional = questionRepository.findOne(Example.of(Question
                .builder()
                .createdBy(user.getId())
                .build()
        ));

        assertThat(questionOptional.isPresent(), is(true));

    }


    @Test
    void should_get_question_detail() {
        User user = this.prepareUser("anyName", "anyEmail");
        Question question = questionService.create("anyTitle", "anyDescription", user.getId());

        Response response = givenDefault()
                .when()
                .get("/questions/" + question.getId());
        response.then().statusCode(200)
                .body("id", is(question.getId()))
                .body("title", is(question.getTitle()))
                .body("description", is(question.getDescription()))
                .body("createdBy", is(question.getCreatedBy()));
    }

    @Test
    void should_get_all_questions_by_page() {
        User user = this.prepareUser("anyName", "anyEmail");
        Question question0 = questionService.create("anyTitle0", "anyDescription0", user.getId());
        Question question1 = questionService.create("anyTitle1", "anyDescription1", user.getId());
        questionService.create("anyTitle2", "anyDescription2", user.getId());

        Response response = givenDefault()
                .param("sort", "createdAt")
                .param("sort", "title")
                .param("size", 2)
                .when()
                .get("/questions");

        response.then().statusCode(200)
                .body("content.size", is(2))
                .body("content.title", hasItems(question0.getTitle(), question1.getTitle()));
    }

    @Test
    void should_update_question() {
        User user = this.prepareUser("anyName", "anyEmail");
        Question question = questionService.create("anyTitle", "anyDescription", user.getId());
        String newTitle = "newTitle";
        String newDescription = "newDescription";

        Response response = givenWithAuthorize(user)
                .body(new HashMap<String, Object>() {
                    {
                        put("title", newTitle);
                        put("description", newDescription);

                    }
                })
                .when()
                .put("/questions/" + question.getId());
        response.then().statusCode(200)
                .body("id", is(question.getId()))
                .body("title", is(newTitle))
                .body("description", is(newDescription));

        Question updatedQuestion = questionRepository.findById(question.getId()).get();
        assertThat(updatedQuestion.getTitle(), is(newTitle));
        assertThat(updatedQuestion.getDescription(), is(newDescription));
    }

    @Test
    void should_delete_question() {
        User user = this.prepareUser("anyName", "anyEmail");
        Question question = questionService.create("anyTitle", "anyDescription", user.getId());

        Response response = givenWithAuthorize(user)
                .when()
                .delete("/questions/" + question.getId());
        response.then().statusCode(200);

        assertThat(questionRepository.existsById(question.getId()), is(false));
    }

    @Test
    void should_create_answer() {
        User user = this.prepareUser("anyName", "anyEmail");
        Question question = questionService.create("anyTitle", "anyDescription", user.getId());
        String content = "content";

        Response response = givenWithAuthorize(user)
                .body(new HashMap<String, Object>() {
                    {
                        put("content", content);
                    }
                })
                .when()
                .post("/questions/" + question.getId() + "/answers");
        response.then().statusCode(201)
                .body("id", isA(String.class))
                .body("content", is(content));

        Optional<Answer> answerOptional = answerRepository.findOne(Example.of(Answer
                .builder()
                .createdBy(user.getId())
                .questionId(question.getId())
                .build()
        ));

        assertThat(answerOptional.isPresent(), is(true));
    }

    @Test
    void should_get_all_answers_by_page() {
        User user = this.prepareUser("anyName", "anyEmail");
        Question question = questionService.create("anyTitle", "anyDescription", user.getId());

        User otherUser = this.prepareUser("otherUserName", "otherUserEmail");
        Answer answer0 = questionService.addAnswer(question.getId(), "content0", user.getId());
        Answer answer1 = questionService.addAnswer(question.getId(), "content1", otherUser.getId());

        Response response = givenDefault()
                .param("sort", "createdAt")
                .when()
                .get("/questions/" + question.getId() + "/answers");

        response.then().statusCode(200)
                .body("content.size", is(2))
                .body("content.content", hasItems(answer0.getContent(), answer1.getContent()));
    }
}
