package com.example.domain.article.service;

import com.example.domain.article.exception.TagArticleException;
import com.example.domain.article.model.Article;
import com.example.domain.article.model.Tag;
import com.example.domain.article.model.TagArticle;
import com.example.domain.article.repository.TagArticleRepository;
import com.example.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class TagArticleService {
    private final TagArticleRepository repository;

    public TagArticle bind(Tag tag, Article article, User user) {
        TagArticle tagArticle = TagArticle.builder()
                .tag(tag)
                .article(article)
                .createdBy(user.getId())
                .lastModifiedAt(Instant.now())
                .build();
        validateUnique(tagArticle);
        return repository.save(tagArticle);
    }

    public List<TagArticle> getByArticle(Article article) {
        return repository.findAll(Example.of(TagArticle.builder()
                .article(article)
                .build()));
    }

    private void validateUnique(TagArticle tagArticle) {
        List<TagArticle> alreadyExistTagArticles = repository.findAll(Example.of(TagArticle.builder()
                .tag(tagArticle.getTag())
                .article(tagArticle.getArticle())
                .build()));

        if (alreadyExistTagArticles.size() > 0) {
            throw TagArticleException.alreadyExist();
        }
    }
}
