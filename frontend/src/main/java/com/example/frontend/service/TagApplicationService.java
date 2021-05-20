package com.example.frontend.service;

import com.example.domain.article.model.Tag;
import com.example.domain.article.service.TagService;
import com.example.domain.auth.model.Authorize;
import com.example.frontend.usecase.CreateTagCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagApplicationService {
    @Autowired
    private TagService service;

    public CreateTagCase.Response create(CreateTagCase.Request request, Authorize authorize) {
        Tag tag = service.create(request.getName(), authorize.getUserId());
        return CreateTagCase.Response.from(tag);
    }
}
