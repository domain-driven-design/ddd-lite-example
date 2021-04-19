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

    public Tag getById(String id) {
        return repository.findById(id).orElseThrow(TagException::notFound);
    }

    public Tag create(String name, User user) {
        Tag tag = Tag.builder()
                .name(name)
                .createdBy(user.getId())
                .lastModifiedAt(Instant.now())
                .build();
        return repository.save(tag);
    }
}
