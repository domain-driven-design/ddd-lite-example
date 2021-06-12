package com.example.business.service;

import com.example.business.usecase.CreateAnswerCase;
import com.example.business.usecase.CreateQuestionCase;
import com.example.business.usecase.GetQuestionCase;
import com.example.business.usecase.GetQuestionDetailCase;
import com.example.business.usecase.UpdateQuestionCase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.question.model.Answer;
import com.example.domain.question.model.Question;
import com.example.domain.question.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class QuestionApplicationService {
    @Autowired
    private QuestionService questionService;

    public CreateQuestionCase.Response create(CreateQuestionCase.Request request, Authorize authorize) {
        Question question = questionService.create(request.getTitle(), request.getDescription(), authorize.getUserId());
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

    public UpdateQuestionCase.Response update(String id, UpdateQuestionCase.Request request, Authorize authorize) {
        Question question = questionService.update(id, request.getTitle(), request.getDescription(), authorize.getUserId());
        return UpdateQuestionCase.Response.from(question);
    }

    public void delete(String id, Authorize authorize) {
        questionService.delete(id, authorize.getUserId());
    }

    public CreateAnswerCase.Response createAnswer(String id, CreateAnswerCase.Request request, Authorize authorize) {
        Answer answer = questionService.addAnswer(id, request.getContent(), authorize.getUserId());
        return CreateAnswerCase.Response.from(answer);
    }
}
