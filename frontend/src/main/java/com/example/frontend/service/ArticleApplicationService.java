package com.example.frontend.service;

import com.example.domain.article.model.Article;
import com.example.domain.article.model.ArticleTag;
import com.example.domain.article.model.Tag;
import com.example.domain.article.repository.ArticleRepository;
import com.example.domain.article.repository.TagRepository;
import com.example.domain.article.service.ArticleService;
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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
@AllArgsConstructor
public class ArticleApplicationService {
    private final ArticleService service;
    private final UserService userService;
    private final ArticleRepository repository;
    private final TagService tagService;
    private final TagRepository tagRepository;

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

        ArticleTag articleTag = service.addTag(article, tag, user);
        return TagArticleCase.Response.from(articleTag);
    }

    public List<GetArticleTagsCase.Response> getTags(String id) {
        Article article = service.getById(id);
        List<ArticleTag> articleTags = article.getTags();
        Map<String, Tag> tagMap = tagRepository.findAllById(
                articleTags.stream()
                        .map(ArticleTag::getTagId)
                        .collect(Collectors.toList()))
                .stream().collect(toMap(Tag::getId, Function.identity()));

        return articleTags.stream()
                .map(articleTag -> GetArticleTagsCase.Response.from(articleTag, tagMap.get(articleTag.getTagId())))
                .collect(Collectors.toList());
    }
}
