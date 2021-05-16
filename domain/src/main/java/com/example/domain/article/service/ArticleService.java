package com.example.domain.article.service;

import com.example.domain.article.exception.ArticleException;
import com.example.domain.article.exception.TagArticleException;
import com.example.domain.article.exception.TagException;
import com.example.domain.article.model.Article;
import com.example.domain.article.model.ArticleTag;
import com.example.domain.article.model.Tag;
import com.example.domain.article.repository.ArticleRepository;
import com.example.domain.article.repository.ArticleTagRepository;
import com.example.domain.article.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository repository;
    @Autowired
    private ArticleTagRepository articleTagRepository;
    @Autowired
    private TagRepository tagRepository;

    public Article get(String id) {
        return this._get(id);
    }

    private Article _get(String id) {
        return repository.findById(id).orElseThrow(ArticleException::notFound);
    }

    public Article create(String title, String content, String operatorId) {
        Article article = Article.builder()
                .title(title)
                .content(content)
                .createdBy(operatorId)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return repository.save(article);
    }

    public Article update(String id, String title, String content, String operatorId) {
        Article article = this._get(id);

        if (!article.getCreatedBy().equals(operatorId)) {
            throw ArticleException.noPermissionUpdate();
        }

        article.setTitle(title);
        article.setContent(content);
        article.setUpdatedAt(Instant.now());
        return repository.save(article);
    }

    public void delete(String id, String operatorId) {
        repository.deleteById(id);
    }

    public ArticleTag addTag(String id, String tagId, String operatorId) {
        Article article = this._get(id);
        Tag tag = tagRepository.findById(tagId).orElseThrow(TagException::notFound);
        // TODO 验证addTag权限

        ArticleTag articleTag = ArticleTag.builder()
                .tagId(tag.getId())
                .articleId(article.getId())
                .createdBy(operatorId)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        validateUnique(articleTag);
        return articleTagRepository.save(articleTag);
    }

    public void deleteTag(String id, String tagId, String operatorId) {

        // TODO 验证删除权限
        articleTagRepository.deleteById(tagId);
    }

    private void validateUnique(ArticleTag articleTag) {
        List<ArticleTag> alreadyExistArticleTags = articleTagRepository.findAll(Example.of(ArticleTag.builder()
                .tagId(articleTag.getTagId())
                .articleId(articleTag.getArticleId())
                .build()));

        if (alreadyExistArticleTags.size() > 0) {
            throw TagArticleException.alreadyExist();
        }
    }
}
