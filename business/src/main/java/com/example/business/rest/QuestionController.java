package com.example.business.rest;

import com.example.business.service.QuestionApplicationService;
import com.example.business.usecase.question.CreateAnswerCase;
import com.example.business.usecase.question.CreateQuestionCase;
import com.example.business.usecase.question.GetAnswerCase;
import com.example.business.usecase.question.GetManagementQuestionCase;
import com.example.business.usecase.question.GetQuestionCase;
import com.example.business.usecase.question.GetQuestionDetailCase;
import com.example.business.usecase.question.UpdateAnswerCase;
import com.example.business.usecase.question.UpdateQuestionCase;
import com.example.business.usecase.question.UpdateQuestionStatusCase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.auth.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/groups/{groupId}/questions")
public class QuestionController {
    @Autowired
    private QuestionApplicationService applicationService;
    @Autowired
    private AuthorizeService authorizeService;

    @PostMapping
    @ResponseStatus(CREATED)
    public CreateQuestionCase.Response createQuestion(@PathVariable String groupId,
                                                      @RequestBody @Valid CreateQuestionCase.Request request) {
        Authorize authorize = authorizeService.current();
        return applicationService.create(request, groupId, authorize);
    }

    @GetMapping("/{id}")
    public GetQuestionDetailCase.Response getQuestionDetail(@PathVariable String id) {
        return applicationService.getDetail(id);
    }

    @GetMapping
    public Page<GetQuestionCase.Response> getQuestionsByPage(@PathVariable String groupId,
                                                             @RequestParam(required = false) String keyword,
                                                             @RequestParam(required = false) String createdBy,
                                                             @PageableDefault Pageable pageable) {
        return applicationService.getByPage(groupId, keyword, createdBy, pageable);
    }

    @GetMapping("/management")
    public Page<GetManagementQuestionCase.Response> getManagementQuestions(@PathVariable String groupId,
                                                                           @RequestParam(required = false) String keyword,
                                                                           @RequestParam(required = false) String createdBy,
                                                                           @PageableDefault Pageable pageable) {
        return applicationService.getManagementQuestions(groupId, keyword, createdBy, pageable);
    }

    @PutMapping("/{id}")
    public UpdateQuestionCase.Response updateQuestion(@PathVariable String groupId,
                                                      @PathVariable String id,
                                                      @RequestBody @Valid UpdateQuestionCase.Request request) {
        Authorize authorize = authorizeService.current();
        return applicationService.update(id, request, groupId, authorize);
    }

    @PutMapping("/{id}/status")
    public UpdateQuestionStatusCase.Response updateQuestionStatus(@PathVariable String groupId,
                                                      @PathVariable String id,
                                                      @RequestBody @Valid UpdateQuestionStatusCase.Request request) {
        Authorize authorize = authorizeService.current();
        return applicationService.updateStatus(id, request, groupId, authorize);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable String groupId, @PathVariable String id) {
        Authorize authorize = authorizeService.current();
        applicationService.delete(id, groupId, authorize);
    }

    @PostMapping("/{id}/answers")
    @ResponseStatus(CREATED)
    public CreateAnswerCase.Response createAnswer(@PathVariable String groupId,
                                                  @PathVariable String id,
                                                  @RequestBody @Valid CreateAnswerCase.Request request) {
        Authorize authorize = authorizeService.current();
        return applicationService.createAnswer(id, request, groupId, authorize);
    }

    @GetMapping("/{id}/answers")
    public Page<GetAnswerCase.Response> getAnswersByPage(@PathVariable String id,
                                                         @PageableDefault Pageable pageable) {
        return applicationService.getAnswersByPage(id, pageable);
    }

    @PutMapping("/{id}/answers/{answerId}")
    public UpdateAnswerCase.Response createAnswer(@PathVariable String groupId,
                                                  @PathVariable String id,
                                                  @PathVariable String answerId,
                                                  @RequestBody @Valid UpdateAnswerCase.Request request) {
        Authorize authorize = authorizeService.current();
        return applicationService.updateAnswer(id, answerId, request, groupId, authorize);
    }

    @DeleteMapping("/{id}/answers/{answerId}")
    public void deleteAnswer(@PathVariable String groupId,
                             @PathVariable String id,
                             @PathVariable String answerId) {
        Authorize authorize = authorizeService.current();
        applicationService.deleteAnswer(id, answerId, groupId, authorize);
    }
}
