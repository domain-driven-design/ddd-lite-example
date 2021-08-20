package com.example.business.rest;

import com.example.TestBase;
import com.example.domain.group.model.Group;
import com.example.domain.group.model.GroupMember;
import com.example.domain.group.service.GroupService;
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
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;

class QuestionControllerTest extends TestBase {
    public static final String DEFAULT_PATH = "/groups/" + Group.DEFAULT + "/questions";

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private GroupService groupService;

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
                .post(DEFAULT_PATH);
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
        assertThat(questionOptional.get().getStatus(), is(Question.Status.OPENED));

    }


    @Test
    void should_get_question_detail() {
        User user = this.prepareUser("anyName", "anyEmail");
        GroupMember member = groupService.getMember(Group.DEFAULT, user.getId());
        Question question = questionService.create("anyTitle", "anyDescription", member);

        Response response = givenDefault()
                .when()
                .get(DEFAULT_PATH + "/" + question.getId());
        response.then().statusCode(200)
                .body("id", is(question.getId()))
                .body("title", is(question.getTitle()))
                .body("description", is(question.getDescription()))
                .body("createdBy", is(question.getCreatedBy()));
    }

    @Test
    void should_query_questions_by_page() {
        User user = this.prepareUser("anyName", "anyEmail");
        User otherUser = this.prepareUser("anyOtherName", "anyOtherEmail");
        GroupMember member = groupService.getMember(Group.DEFAULT, user.getId());
        GroupMember otherMember = groupService.getMember(Group.DEFAULT, otherUser.getId());
        Question question0 = questionService.create("anyTitle0", "anyDescription0", member);
        Question question1 = questionService.create("anyTitle1", "anyDescription1", member);
        questionService.create("anyTitle2", "anyDescription2", member);
        Question question3 = questionService.create("anyTitle3", "anyDescription3", otherMember);

        givenDefault()
                .param("sort", "createdAt")
                .param("sort", "title")
                .param("size", 2)
                .when()
                .get(DEFAULT_PATH)
                .then()
                .statusCode(200)
                .body("content.size", is(2))
                .body("content.title", hasItems(question0.getTitle(), question1.getTitle()))
                .body("content.description", hasItems(question0.getDescription(), question1.getDescription()));


        givenDefault()
                .param("sort", "createdAt")
                .param("sort", "title")
                .param("keyword", "Title1")
                .param("size", 2)
                .when()
                .get(DEFAULT_PATH)
                .then()
                .statusCode(200)
                .body("content.size", is(1))
                .body("content[0].title", is(question1.getTitle()))
                .body("content[0].description", is(question1.getDescription()));

        givenDefault()
                .param("sort", "createdAt")
                .param("sort", "title")
                .param("createdBy", otherUser.getId())
                .param("size", 1)
                .when()
                .get(DEFAULT_PATH)
                .then()
                .statusCode(200)
                .body("content.size", is(1))
                .body("content[0].title", is(question3.getTitle()))
                .body("content[0].description", is(question3.getDescription()));
    }

    @Test
    void should_get_management_questions_by_page() {
        User user = this.prepareUser("anyName", "anyEmail");
        User otherUser = this.prepareUser("anyOtherName", "anyOtherEmail");
        GroupMember member = groupService.getMember(Group.DEFAULT, user.getId());
        GroupMember otherMember = groupService.getMember(Group.DEFAULT, otherUser.getId());
        Question question0 = questionService.create("anyTitle0", "anyDescription0", member);
        Question question1 = questionService.create("anyTitle1", "anyDescription1", member);
        questionService.create("anyTitle2", "anyDescription2", member);
        Question question3 = questionService.create("anyTitle3", "anyDescription3", otherMember);

        givenDefault()
                .param("sort", "createdAt")
                .param("sort", "title")
                .param("size", 2)
                .when()
                .get(DEFAULT_PATH + "/management")
                .then()
                .statusCode(200)
                .body("content.size", is(2))
                .body("content.title", hasItems(question0.getTitle(), question1.getTitle()))
                .body("content.description", hasItems(question0.getDescription(), question1.getDescription()))
                .body("content[0].creator.id", is(user.getId()))
                .body("content[0].creator.name", is(user.getName()));


        givenDefault()
                .param("sort", "createdAt")
                .param("sort", "title")
                .param("keyword", "Title1")
                .param("size", 2)
                .when()
                .get(DEFAULT_PATH + "/management")
                .then()
                .statusCode(200)
                .body("content.size", is(1))
                .body("content[0].title", is(question1.getTitle()))
                .body("content[0].description", is(question1.getDescription()));

        givenDefault()
                .param("sort", "createdAt")
                .param("sort", "title")
                .param("createdBy", otherUser.getId())
                .param("size", 1)
                .when()
                .get(DEFAULT_PATH + "/management")
                .then()
                .statusCode(200)
                .body("content.size", is(1))
                .body("content[0].title", is(question3.getTitle()))
                .body("content[0].description", is(question3.getDescription()));
    }

    @Test
    void should_update_question() {
        User user = this.prepareUser("anyName", "anyEmail");
        GroupMember member = groupService.getMember(Group.DEFAULT, user.getId());
        Question question = questionService.create("anyTitle", "anyDescription", member);
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
                .put(DEFAULT_PATH + "/" + question.getId());
        response.then().statusCode(200)
                .body("id", is(question.getId()))
                .body("title", is(newTitle))
                .body("description", is(newDescription));

        Question updatedQuestion = questionRepository.findById(question.getId()).get();
        assertThat(updatedQuestion.getTitle(), is(newTitle));
        assertThat(updatedQuestion.getDescription(), is(newDescription));
    }

    @Test
    void should_update_question_status_by_creator() {
        User user = this.prepareUser("anyName", "anyEmail");
        GroupMember member = groupService.getMember(Group.DEFAULT, user.getId());
        Question question = questionService.create("anyTitle", "anyDescription", member);

        Response response = givenWithAuthorize(user)
                .body(new HashMap<String, Object>() {
                    {
                        put("status", Question.Status.CLOSED);
                    }
                })
                .when()
                .put(DEFAULT_PATH + "/" + question.getId() + "/status");
        response.then().statusCode(200)
                .body("id", is(question.getId()))
                .body("status", is(Question.Status.CLOSED.name()));

        Question updatedQuestion = questionRepository.findById(question.getId()).get();
        assertThat(updatedQuestion.getStatus(), is(Question.Status.CLOSED));

        givenWithAuthorize(user)
                .body(new HashMap<String, Object>() {
                    {
                        put("status", Question.Status.OPENED);
                    }
                })
                .when()
                .put(DEFAULT_PATH + "/" + question.getId() + "/status");
        updatedQuestion = questionRepository.findById(question.getId()).get();
        assertThat(updatedQuestion.getStatus(), is(Question.Status.OPENED));

    }

    @Test
    void should_update_question_status_by_group_admin_and_not_creator() {
        User user = this.prepareUser("anyName", "anyEmail");
        Group group = groupService.create("anyGroupName", "", user.getId());
        GroupMember groupOwner = group.getMembers().get(0);
        User otherUser = this.prepareUser("otherUserName", "otherUserEmail");
        GroupMember groupAdmin = addGroupAdmin(group, otherUser, groupOwner);

        Question question = questionService.create("anyTitle", "anyDescription", groupOwner);

        Response response = givenWithAuthorize(otherUser)
                .body(new HashMap<String, Object>() {
                    {
                        put("status", Question.Status.CLOSED);
                    }
                })
                .when()
                .put(DEFAULT_PATH + "/" + question.getId() + "/status");
        response.then().statusCode(200)
                .body("id", is(question.getId()))
                .body("status", is(Question.Status.CLOSED.name()));

        Question updatedQuestion = questionRepository.findById(question.getId()).get();
        assertThat(updatedQuestion.getStatus(), is(Question.Status.CLOSED));

        givenWithAuthorize(user)
                .body(new HashMap<String, Object>() {
                    {
                        put("status", Question.Status.OPENED);
                    }
                })
                .when()
                .put(DEFAULT_PATH + "/" + question.getId() + "/status");
        updatedQuestion = questionRepository.findById(question.getId()).get();
        assertThat(updatedQuestion.getStatus(), is(Question.Status.OPENED));

    }

    @Test
    void should_update_question_status_by_group_owner_and_not_creator() {
        User user = this.prepareUser("anyName", "anyEmail");
        Group group = groupService.create("anyGroupName", "", user.getId());
        GroupMember groupOwner = group.getMembers().get(0);
        User otherUser = this.prepareUser("otherUserName", "otherUserEmail");
        GroupMember groupAdmin = addGroupAdmin(group, otherUser, groupOwner);

        Question question = questionService.create("anyTitle", "anyDescription", groupAdmin);

        Response response = givenWithAuthorize(user)
                .body(new HashMap<String, Object>() {
                    {
                        put("status", Question.Status.CLOSED);
                    }
                })
                .when()
                .put(DEFAULT_PATH + "/" + question.getId() + "/status");
        response.then().statusCode(200)
                .body("id", is(question.getId()))
                .body("status", is(Question.Status.CLOSED.name()));

        Question updatedQuestion = questionRepository.findById(question.getId()).get();
        assertThat(updatedQuestion.getStatus(), is(Question.Status.CLOSED));

        givenWithAuthorize(user)
                .body(new HashMap<String, Object>() {
                    {
                        put("status", Question.Status.OPENED);
                    }
                })
                .when()
                .put(DEFAULT_PATH + "/" + question.getId() + "/status");
        updatedQuestion = questionRepository.findById(question.getId()).get();
        assertThat(updatedQuestion.getStatus(), is(Question.Status.OPENED));

    }

    @Test
    void should_delete_question_by_creator() {
        User user = this.prepareUser("anyName", "anyEmail");
        GroupMember member = groupService.getMember(Group.DEFAULT, user.getId());
        User otherUser = this.prepareUser("otherUserName", "otherUserEmail");
        GroupMember otherMember = groupService.getMember(Group.DEFAULT, otherUser.getId());

        Question question = questionService.create("anyTitle", "anyDescription", member);
        String questionId = question.getId();

        questionService.addAnswer(questionId, "content0", member);
        questionService.addAnswer(questionId, "content1", otherMember);

        Response response = givenWithAuthorize(user)
                .when()
                .delete(DEFAULT_PATH + "/" + questionId);
        response.then().statusCode(200);

        assertThat(questionRepository.existsById(questionId), is(false));

        List<Answer> answers = answerRepository.findAll(Example.of(Answer.builder().questionId(questionId).build()));
        assertThat(answers, hasSize(0));
    }

    @Test
    void should_delete_question_by_group_admin_and_not_creator() {
        User user = this.prepareUser("anyName", "anyEmail");
        Group group = groupService.create("anyGroupName", "", user.getId());
        GroupMember groupOwner = group.getMembers().get(0);
        User otherUser = this.prepareUser("otherUserName", "otherUserEmail");
        GroupMember groupAdmin = addGroupAdmin(group, otherUser, groupOwner);

        Question question = questionService.create("anyTitle", "anyDescription", groupOwner);
        String questionId = question.getId();

        questionService.addAnswer(questionId, "content0", groupOwner);
        questionService.addAnswer(questionId, "content1", groupAdmin);

        Response response = givenWithAuthorize(otherUser)
                .when()
                .delete(DEFAULT_PATH + "/" + questionId);
        response.then().statusCode(200);

        assertThat(questionRepository.existsById(questionId), is(false));

        List<Answer> answers = answerRepository.findAll(Example.of(Answer.builder().questionId(questionId).build()));
        assertThat(answers, hasSize(0));
    }

    @Test
    void should_delete_question_by_group_owner_and_not_creator() {
        User user = this.prepareUser("anyName", "anyEmail");
        Group group = groupService.create("anyGroupName", "", user.getId());
        GroupMember groupOwner = group.getMembers().get(0);
        User otherUser = this.prepareUser("otherUserName", "otherUserEmail");
        GroupMember groupAdmin = addGroupAdmin(group, otherUser, groupOwner);

        Question question = questionService.create("anyTitle", "anyDescription", groupAdmin);
        String questionId = question.getId();

        questionService.addAnswer(questionId, "content0", groupOwner);
        questionService.addAnswer(questionId, "content1", groupAdmin);

        Response response = givenWithAuthorize(user)
                .when()
                .delete(DEFAULT_PATH + "/" + questionId);
        response.then().statusCode(200);

        assertThat(questionRepository.existsById(questionId), is(false));

        List<Answer> answers = answerRepository.findAll(Example.of(Answer.builder().questionId(questionId).build()));
        assertThat(answers, hasSize(0));
    }

    @Test
    void should_create_answer() {
        User user = this.prepareUser("anyName", "anyEmail");
        GroupMember member = groupService.getMember(Group.DEFAULT, user.getId());
        Question question = questionService.create("anyTitle", "anyDescription", member);
        String content = "content";

        Response response = givenWithAuthorize(user)
                .body(new HashMap<String, Object>() {
                    {
                        put("content", content);
                    }
                })
                .when()
                .post(DEFAULT_PATH + "/" + question.getId() + "/answers");
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
        GroupMember member = groupService.getMember(Group.DEFAULT, user.getId());
        User otherUser = this.prepareUser("otherUserName", "otherUserEmail");
        GroupMember otherMember = groupService.getMember(Group.DEFAULT, otherUser.getId());
        Question question = questionService.create("anyTitle", "anyDescription", member);

        Answer answer0 = questionService.addAnswer(question.getId(), "content0", member);
        Answer answer1 = questionService.addAnswer(question.getId(), "content1", otherMember);

        Response response = givenDefault()
                .param("sort", "createdAt")
                .when()
                .get(DEFAULT_PATH + "/" + question.getId() + "/answers");

        response.then().statusCode(200)
                .body("content.size", is(2))
                .body("content.content", hasItems(answer0.getContent(), answer1.getContent()));
    }

    @Test
    void should_update_answer() {
        User user = this.prepareUser("anyName", "anyEmail");
        GroupMember member = groupService.getMember(Group.DEFAULT, user.getId());
        Question question = questionService.create("anyTitle", "anyDescription", member);
        Answer answer = questionService.addAnswer(question.getId(), "content", member);
        String newContent = "newContent";

        Response response = givenWithAuthorize(user)
                .body(new HashMap<String, Object>() {
                    {
                        put("content", newContent);
                    }
                })
                .when()
                .put(DEFAULT_PATH + "/" + question.getId() + "/answers/" + answer.getId());
        response.then().statusCode(200)
                .body("id", isA(String.class))
                .body("content", is(newContent));

        Answer updatedAnswer = answerRepository.findById(answer.getId()).get();

        assertThat(updatedAnswer.getContent(), is(newContent));
    }

    @Test
    void should_delete_answer_by_creator() {
        User user = this.prepareUser("anyName", "anyEmail");
        GroupMember member = groupService.getMember(Group.DEFAULT, user.getId());
        Question question = questionService.create("anyTitle", "anyDescription", member);
        Answer answer = questionService.addAnswer(question.getId(), "content", member);

        Response response = givenWithAuthorize(user)
                .when()
                .delete(DEFAULT_PATH + "/" + question.getId() + "/answers/" + answer.getId());
        response.then().statusCode(200);

        assertThat(answerRepository.findById(answer.getId()).isPresent(), is(false));
    }

    @Test
    void should_delete_answer_by_group_admin_and_not_creator() {
        User user = this.prepareUser("anyName", "anyEmail");
        Group group = groupService.create("anyGroupName", "", user.getId());
        GroupMember groupOwner = group.getMembers().get(0);
        User otherUser = this.prepareUser("otherUserName", "otherUserEmail");
        GroupMember groupAdmin = addGroupAdmin(group, otherUser, groupOwner);

        Question question = questionService.create("anyTitle", "anyDescription", groupAdmin);
        Answer answer = questionService.addAnswer(question.getId(), "content", groupAdmin);

        Response response = givenWithAuthorize(otherUser)
                .when()
                .delete(DEFAULT_PATH + "/" + question.getId() + "/answers/" + answer.getId());
        response.then().statusCode(200);

        assertThat(answerRepository.findById(answer.getId()).isPresent(), is(false));
    }

    @Test
    void should_delete_answer_by_group_owner_and_not_creator() {
        User user = this.prepareUser("anyName", "anyEmail");
        Group group = groupService.create("anyGroupName", "", user.getId());
        GroupMember groupOwner = group.getMembers().get(0);
        User otherUser = this.prepareUser("otherUserName", "otherUserEmail");
        GroupMember groupAdmin = addGroupAdmin(group, otherUser, groupOwner);

        Question question = questionService.create("anyTitle", "anyDescription", groupAdmin);
        Answer answer = questionService.addAnswer(question.getId(), "content", groupAdmin);

        Response response = givenWithAuthorize(user)
                .when()
                .delete(DEFAULT_PATH + "/" + question.getId() + "/answers/" + answer.getId());
        response.then().statusCode(200);

        assertThat(answerRepository.findById(answer.getId()).isPresent(), is(false));
    }


    private GroupMember addGroupAdmin(Group group, User user, GroupMember operator) {
        GroupMember otherMember = groupService.addNormalMember(group.getId(), user.getId());
        return groupService.changeMemberRole(group.getId(), otherMember.getUserId(), GroupMember.Role.ADMIN, operator.getUserId());
    }
}
