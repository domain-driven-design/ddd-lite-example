package com.example.frontend.service;

import com.example.domain.article.model.Article;
import com.example.domain.article.model.Tag;
import com.example.domain.article.model.TagArticle;
import com.example.domain.article.repository.ArticleRepository;
import com.example.domain.article.service.ArticleService;
import com.example.domain.article.service.TagArticleService;
import com.example.domain.article.service.TagService;
import com.example.domain.auth.model.Authorize;
import com.example.domain.user.model.User;
import com.example.domain.user.service.UserService;
import com.example.frontend.usecase.CreateArticleCase;
import com.example.frontend.usecase.GetArticleDetailCase;
import com.example.frontend.usecase.GetArticleTagsCase;
import com.example.frontend.usecase.GetArticlesCase;
import com.example.frontend.usecase.TagArticleCase;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArticleApplicationService {
    private final ArticleService service;
    private final UserService userService;
    private final ArticleRepository repository;
    private final TagService tagService;
    private final TagArticleService tagArticleService;

    public CreateArticleCase.Response create(CreateArticleCase.Request request, Authorize authorize) {
        User user = userService.getById(authorize.getUserId());
        Article article = service.create(request.getTitle(), request.getContent(), user);
        return CreateArticleCase.Response.from(article);
    }

    public GetArticleDetailCase.Response getDetail(String id) {
        Article article = service.getById(id);
        return GetArticleDetailCase.Response.from(article);
    }

    public Page<GetArticlesCase.Response> getByPage(Pageable pageable) {
        return repository.findAll(pageable)
                .map(GetArticlesCase.Response::from);
    }

    public TagArticleCase.Response tagArticle(String id, String tagId, Authorize authorize) {
        User user = userService.getById(authorize.getUserId());
        Article article = service.getById(id);
        Tag tag = tagService.getById(tagId);

        TagArticle tagArticle = tagArticleService.bind(tag, article, user);
        return TagArticleCase.Response.from(tagArticle);
    }

    public List<GetArticleTagsCase.Response> getTags(String id) {
        Article article = service.getById(id);
        List<TagArticle> tagArticles = tagArticleService.getByArticle(article);

        return tagArticles.stream().map(GetArticleTagsCase.Response::from)
                .collect(Collectors.toList());
    }
}
