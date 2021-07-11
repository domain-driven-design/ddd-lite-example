package com.example.business.service;

import com.example.business.usecase.query.QueryAnswersCase;
import com.example.domain.question.model.Answer;
import com.example.domain.question.model.Question;
import com.example.domain.question.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
public class QueryApplicationService {
    @Autowired
    private QuestionService questionService;

    public Page<QueryAnswersCase.Response> queryAnswers(String userId, Pageable pageable) {
        Specification<Answer> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(Answer.Fields.createdBy), userId);
        Page<Answer> page = questionService.findAllAnswers(specification, pageable);

        List<String> questionIds = page.getContent().stream()
                .map(Answer::getQuestionId)
                .distinct()
                .collect(Collectors.toList());
        Specification<Question> questionSpecification = (root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get(Question.Fields.id)).value(questionIds);
        Map<String, Question> questionMap = questionService.findAll(questionSpecification).stream()
                .collect(toMap(Question::getId, Function.identity()));

        return page.map(answer -> QueryAnswersCase.Response.from(questionMap.get(answer.getQuestionId()), answer));
    }
}
