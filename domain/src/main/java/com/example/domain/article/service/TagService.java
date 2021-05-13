package com.example.domain.article.service;

import com.example.domain.article.exception.TagException;
import com.example.domain.article.model.Tag;
import com.example.domain.article.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TagService {
    @Autowired
    private TagRepository repository;

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
        repository.deleteById(id);
    }
}
