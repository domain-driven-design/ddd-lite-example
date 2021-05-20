package com.example.frontend.rest;

import com.example.domain.auth.AuthorizeContextHolder;
import com.example.domain.auth.model.Authorize;
import com.example.frontend.service.TagApplicationService;
import com.example.frontend.usecase.CreateTagCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/tags")
public class TagController {
    @Autowired
    private TagApplicationService applicationService;

    @PostMapping
    @ResponseStatus(CREATED)
    public CreateTagCase.Response createTag(@RequestBody CreateTagCase.Request request) {
        Authorize authorize = AuthorizeContextHolder.getContext();
        return applicationService.create(request, authorize);
    }
}
