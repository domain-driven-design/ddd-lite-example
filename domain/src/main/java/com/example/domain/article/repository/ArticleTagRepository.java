package com.example.domain.article.repository;

import com.example.domain.article.model.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleTagRepository extends JpaRepository<ArticleTag, String> {
    void deleteAllByTagId(String tagId);
}