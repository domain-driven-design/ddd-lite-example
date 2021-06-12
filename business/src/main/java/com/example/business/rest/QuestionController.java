package com.example.business.rest;

import com.example.business.service.QuestionApplicationService;
import com.example.business.usecase.CreateQuestionCase;
import com.example.business.usecase.GetQuestionCase;
import com.example.business.usecase.GetQuestionDetailCase;
import com.example.business.usecase.UpdateQuestionCase;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    @Autowired
    private QuestionApplicationService applicationService;
    @Autowired
    private AuthorizeService authorizeService;

    @PostMapping
    @ResponseStatus(CREATED)
    public CreateQuestionCase.Response createQuestion(@RequestBody CreateQuestionCase.Request request) {
        Authorize authorize = authorizeService.current();
        return applicationService.create(request, authorize);
    }

    @GetMapping("/{id}")
    public GetQuestionDetailCase.Response getQuestionDetail(@PathVariable String id) {
        return applicationService.getDetail(id);
    }

    @GetMapping()
    public Page<GetQuestionCase.Response> getQuestionsByPage(@PageableDefault Pageable pageable) {
        return applicationService.getByPage(pageable);
    }

    @PutMapping("/{id}")
    public UpdateQuestionCase.Response updateQuestion(@PathVariable String id,
                                                      @RequestBody UpdateQuestionCase.Request request) {
        Authorize authorize = authorizeService.current();
        return applicationService.update(id, request, authorize);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable String id) {
        Authorize authorize = authorizeService.current();
        applicationService.delete(id, authorize);
    }


}
