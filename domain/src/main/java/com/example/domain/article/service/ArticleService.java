package com.example.domain.article.service;

import com.example.domain.article.exception.ArticleException;
import com.example.domain.article.model.Article;
import com.example.domain.article.repository.ArticleRepository;
import com.example.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class ArticleService {
    private final ArticleRepository repository;

    public Article getById(String id) {
        return repository.findById(id).orElseThrow(ArticleException::notFound);
    }

    public Article create(String title, String content, User user) {
        Article article = Article.builder()
                .title(title)
                .content(content)
                .createdBy(user.getId())
                .lastModifiedAt(Instant.now())
                .build();
        return repository.save(article);
    }

    public Article update(Article article, String title, String content) {
        article.setTitle(title);
        article.setContent(content);
        article.setLastModifiedAt(Instant.now());
        return repository.save(article);
    }

    public void delete(Article article) {
        repository.delete(article);
    }
}
