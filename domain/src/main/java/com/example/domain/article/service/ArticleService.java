package com.example.domain.article.service;

import com.example.domain.article.exception.ArticleException;
import com.example.domain.article.exception.TagArticleException;
import com.example.domain.article.model.Article;
import com.example.domain.article.model.Tag;
import com.example.domain.article.model.ArticleTag;
import com.example.domain.article.repository.ArticleRepository;
import com.example.domain.article.repository.TagArticleRepository;
import com.example.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class ArticleService {
    private final ArticleRepository repository;
    private final TagArticleRepository tagArticleRepository;

    public Article getById(String id) {
        return repository.findById(id).orElseThrow(ArticleException::notFound);
    }

    public Article create(String title, String content, User user) {
        Article article = Article.builder()
                .title(title)
                .content(content)
                .createdBy(user.getId())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return repository.save(article);
    }

    public Article update(Article article, String title, String content) {
        article.setTitle(title);
        article.setContent(content);
        article.setUpdatedAt(Instant.now());
        return repository.save(article);
    }

    public void delete(Article article) {
        repository.delete(article);
    }

    public ArticleTag addTag(Article article, Tag tag, User user) {
        ArticleTag articleTag = ArticleTag.builder()
                .tagId(tag.getId())
                .articleId(article.getId())
                .createdBy(user.getId())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        validateUnique(articleTag);
        return tagArticleRepository.save(articleTag);
    }

    private void validateUnique(ArticleTag articleTag) {
        List<ArticleTag> alreadyExistArticleTags = tagArticleRepository.findAll(Example.of(ArticleTag.builder()
                .tagId(articleTag.getTagId())
                .articleId(articleTag.getArticleId())
                .build()));

        if (alreadyExistArticleTags.size() > 0) {
            throw TagArticleException.alreadyExist();
        }
    }
}
