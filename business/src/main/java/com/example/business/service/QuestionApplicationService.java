package com.example.business.service;

import com.example.business.usecase.question.CreateAnswerCase;
import com.example.business.usecase.question.CreateQuestionCase;
import com.example.business.usecase.question.GetAnswerCase;
import com.example.business.usecase.question.GetQuestionCase;
import com.example.business.usecase.question.GetQuestionDetailCase;
import com.example.business.usecase.question.UpdateAnswerCase;
import com.example.business.usecase.question.UpdateQuestionCase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.group.service.GroupService;
import com.example.domain.question.model.Answer;
import com.example.domain.question.model.Question;
import com.example.domain.question.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class QuestionApplicationService {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private GroupService groupService;

    public CreateQuestionCase.Response create(CreateQuestionCase.Request request, String groupId, Authorize authorize) {
        groupService.checkMember(groupId, authorize.getUserId());

        Question question = questionService.create(request.getTitle(), request.getDescription(), groupId, authorize.getUserId());

        return CreateQuestionCase.Response.from(question);
    }

    public GetQuestionDetailCase.Response getDetail(String id) {
        Question question = questionService.get(id);

        return GetQuestionDetailCase.Response.from(question);
    }

    public Page<GetQuestionCase.Response> getByPage(Pageable pageable) {
        Page<Question> questionPage = questionService.findAll(null, pageable);

        return questionPage.map(GetQuestionCase.Response::from);
    }

    public UpdateQuestionCase.Response update(String id, UpdateQuestionCase.Request request, String groupId, Authorize authorize) {
        groupService.checkMember(groupId, authorize.getUserId());

        Question question = questionService.update(id, request.getTitle(), request.getDescription(), authorize.getUserId());

        return UpdateQuestionCase.Response.from(question);
    }

    public void delete(String id, String groupId, Authorize authorize) {
        groupService.checkMember(groupId, authorize.getUserId());

        questionService.delete(id, authorize.getUserId());
    }

    public CreateAnswerCase.Response createAnswer(String id, CreateAnswerCase.Request request, String groupId, Authorize authorize) {
        groupService.checkMember(groupId, authorize.getUserId());

        Answer answer = questionService.addAnswer(id, request.getContent(), authorize.getUserId());

        return CreateAnswerCase.Response.from(answer);
    }

    public Page<GetAnswerCase.Response> getAnswersByPage(String id, Pageable pageable) {
        Specification<Answer> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(Answer.Fields.questionId), id);
        Page<Answer> answerPage = questionService.findAllAnswers(specification, pageable);

        return answerPage.map(GetAnswerCase.Response::from);
    }

    public UpdateAnswerCase.Response updateAnswer(String id, String answerId, UpdateAnswerCase.Request request, String groupId, Authorize authorize) {
        groupService.checkMember(groupId, authorize.getUserId());

        Answer answer = questionService.updateAnswer(id, answerId, request.getContent(), authorize.getUserId());

        return UpdateAnswerCase.Response.from(answer);
    }

    public void deleteAnswer(String id, String answerId, String groupId, Authorize authorize) {
        groupService.checkMember(groupId, authorize.getUserId());

        questionService.deleteAnswer(id, answerId, authorize.getUserId());
    }
}
