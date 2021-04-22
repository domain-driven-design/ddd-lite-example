package com.example.domain.article.service;

import com.example.domain.article.exception.TagException;
import com.example.domain.article.model.Tag;
import com.example.domain.article.repository.TagRepository;
import com.example.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class TagService {
    private final TagRepository repository;
    private final ArticleService articleService;

    public Tag get(String id) {
        return this._get(id);
    }

    private Tag _get(String id) {
        return repository.findById(id).orElseThrow(TagException::notFound);
    }

    public Tag create(String name, String createdBy) {
        Tag tag = Tag.builder()
                .name(name)
                .createdBy(createdBy)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return repository.save(tag);
    }

    public void delete(String id, String deleteBy) {
        // TODO 是否验证delete权限？
        articleService.cleanRelatedTags(id, deleteBy);
        repository.deleteById(id);
    }
}
