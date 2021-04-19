package com.example.frontend.rest;

import com.example.domain.article.model.TagArticle;
import com.example.domain.auth.model.Authorize;
import com.example.domain.auth.service.AuthorizeService;
import com.example.frontend.service.ArticleApplicationService;
import com.example.frontend.usecase.CreateArticleCase;
import com.example.frontend.usecase.GetArticleDetailCase;
import com.example.frontend.usecase.GetArticleTagsCase;
import com.example.frontend.usecase.GetArticlesCase;
import com.example.frontend.usecase.TagArticleCase;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("articles")
@AllArgsConstructor
public class ArticleController {
    private final ArticleApplicationService applicationService;
    private final AuthorizeService authorizeService;

    @PostMapping
    @ResponseStatus(CREATED)
    public CreateArticleCase.Response createArticle(@RequestParam(value = "token") String token,
                                                    @RequestBody CreateArticleCase.Request request) {
        Authorize authorize = authorizeService.getById(token);
        return applicationService.create(request, authorize);
    }

    @GetMapping("/{id}")
    public GetArticleDetailCase.Response getArticleDetail(@PathVariable("id") String id) {
        return applicationService.getDetail(id);
    }

    @GetMapping()
    public Page<GetArticlesCase.Response> getArticles(@PageableDefault(sort = "lastModifiedAt") Pageable pageable) {
        return applicationService.getByPage(pageable);
    }

    @PostMapping("/{id}/tags/{tagId}")
    @ResponseStatus(CREATED)
    public TagArticleCase.Response createArticle(@RequestParam(value = "token") String token,
                                                 @PathVariable("id") String id,
                                                 @PathVariable("tagId") String tagId) {
        Authorize authorize = authorizeService.getById(token);
        return applicationService.tagArticle(id, tagId, authorize);
    }

    @GetMapping("/{id}/tags")
    public List<GetArticleTagsCase.Response> getTags(@PathVariable("id") String id) {
        return applicationService.getTags(id);
    }
}
