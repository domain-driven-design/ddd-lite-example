package com.example.admin.rest;

import com.example.admin.service.QuestionManagementApplicationService;
import com.example.admin.usecases.question.GetQuestionsCase;
import com.example.admin.usecases.question.UpdateQuestionStatusCase;
import com.example.domain.auth.service.AuthorizeService;
import com.example.domain.group.GroupContextHolder;
import com.example.domain.user.model.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/management/questions")
public class QuestionManagementController {
    @Autowired
    private QuestionManagementApplicationService applicationService;
    @Autowired
    private AuthorizeService authorizeService;

    @GetMapping
    public Page<GetQuestionsCase.Response> getQuestions(@RequestParam(required = false) String keyword,
                                                        Pageable pageable) {
        return applicationService.getQuestions(keyword, pageable);
    }

    @PutMapping("/{id}/status")
    public UpdateQuestionStatusCase.Response updateQuestionStatus(@PathVariable String id,
                                                                  @RequestBody
                                                                  @Valid UpdateQuestionStatusCase.Request request) {
        Operator operator = authorizeService.getOperator();
        String groupId = GroupContextHolder.getContext();
        return applicationService.updateStatus(id, request, groupId, operator);
    }
}
