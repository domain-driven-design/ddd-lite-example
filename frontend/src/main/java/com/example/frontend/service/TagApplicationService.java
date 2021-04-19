package com.example.frontend.service;

import com.example.domain.article.model.Tag;
import com.example.domain.article.repository.TagRepository;
import com.example.domain.article.service.TagService;
import com.example.domain.auth.model.Authorize;
import com.example.domain.user.model.User;
import com.example.domain.user.service.UserService;
import com.example.frontend.usecase.CreateTagCase;
import com.example.frontend.usecase.GetTagDetailCase;
import com.example.frontend.usecase.GetTagsCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TagApplicationService {
    private final TagService service;
    private final UserService userService;
    private final TagRepository repository;

    public CreateTagCase.Response create(CreateTagCase.Request request, Authorize authorize) {
        User user = userService.getById(authorize.getUserId());
        Tag tag = service.create(request.getName(), user);
        return CreateTagCase.Response.from(tag);
    }

    public GetTagDetailCase.Response getDetail(String id) {
        Tag tag = service.getById(id);
        return GetTagDetailCase.Response.from(tag);
    }

    public List<GetTagsCase.Response> getAll() {
        return repository.findAll()
                .stream()
                .map(GetTagsCase.Response::from)
                .collect(Collectors.toList());
    }
}
