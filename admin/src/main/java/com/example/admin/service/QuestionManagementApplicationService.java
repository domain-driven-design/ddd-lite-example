package com.example.admin.service;

import com.example.admin.common.GroupCriteria;
import com.example.admin.common.UserCriteria;
import com.example.admin.usecases.question.GetQuestionsCase;
import com.example.admin.usecases.question.UpdateQuestionStatusCase;
import com.example.domain.group.model.Group;
import com.example.domain.group.model.GroupOperator;
import com.example.domain.group.service.GroupService;
import com.example.domain.question.model.Question;
import com.example.domain.question.service.QuestionService;
import com.example.domain.user.model.Operator;
import com.example.domain.user.model.User;
import com.example.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
public class QuestionManagementApplicationService {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;

    public Page<GetQuestionsCase.Response> getQuestions(String keyword, Pageable pageable) {
        Specification<Question> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            if (keyword != null) {
                List<Predicate> keywordPredicateList = new ArrayList<>();
                keywordPredicateList.add(criteriaBuilder.like(root.get(Question.Fields.title), '%' + keyword + '%'));
                predicateList.add(criteriaBuilder.or(keywordPredicateList.toArray(new Predicate[0])));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };

        Page<Question> page = questionService.findAll(spec, pageable);

        Set<String> creatorIds = page.getContent().stream().map(Question::getCreatedBy).collect(Collectors.toSet());
        Map<String, User> creatorMap = userService.findAll(UserCriteria.getUsersIn(creatorIds)).stream()
                .collect(toMap(User::getId, Function.identity()));

        Set<String> groupIds = page.getContent().stream().map(Question::getGroupId).collect(Collectors.toSet());
        Map<String, Group> groupMap = groupService.findAll(GroupCriteria.getGroupsIn(groupIds)).stream()
                .collect(toMap(Group::getId, Function.identity()));

        return questionService.findAll(spec, pageable)
                .map(question -> GetQuestionsCase.Response.from(
                        question, creatorMap.get(question.getCreatedBy()), groupMap.get(question.getGroupId()))
                );
    }

    public UpdateQuestionStatusCase.Response updateStatus(String id, UpdateQuestionStatusCase.Request request,
                                                          String groupId,
                                                          Operator operator) {
        GroupOperator groupOperator = groupService.getOperator(groupId, operator);

        Question question = questionService.updateStatus(id, request.getStatus(), groupOperator);

        return UpdateQuestionStatusCase.Response.from(question);
    }

    public void delete(String id, String groupId, Operator operator) {
        GroupOperator groupOperator = groupService.getOperator(groupId, operator);

        questionService.delete(id, groupOperator);
    }
}
