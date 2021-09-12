package com.example.admin.rest;

import com.example.admin.service.QuestionManagementApplicationService;
import com.example.admin.usecases.question.GetQuestionsCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management/questions")
public class QuestionManagementController {
    @Autowired
    private QuestionManagementApplicationService applicationService;

    @GetMapping
    public Page<GetQuestionsCase.Response> getQuestions(@RequestParam(required = false) String keyword,
                                                        Pageable pageable) {
        return applicationService.getQuestions(keyword, pageable);
    }
}
