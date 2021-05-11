package com.example.frontend.service;

import com.example.domain.article.model.Tag;
import com.example.domain.article.repository.TagRepository;
import com.example.domain.article.service.TagService;
import com.example.domain.auth.model.Authorize;
import com.example.frontend.usecase.CreateTagCase;
import com.example.frontend.usecase.GetTagDetailCase;
import com.example.frontend.usecase.GetTagsCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagApplicationService {
    @Autowired
    private TagService service;
    @Autowired
    private TagRepository repository;

    public CreateTagCase.Response create(CreateTagCase.Request request, Authorize authorize) {
        Tag tag = service.create(request.getName(), authorize.getUserId());
        return CreateTagCase.Response.from(tag);
    }

    public GetTagDetailCase.Response getDetail(String id) {
        Tag tag = service.get(id);
        return GetTagDetailCase.Response.from(tag);
    }

    public List<GetTagsCase.Response> getAll() {
        return repository.findAll()
                .stream()
                .map(GetTagsCase.Response::from)
                .collect(Collectors.toList());
    }
}
