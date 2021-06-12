package com.example.domain.question.service;

import com.example.domain.question.exception.QuestionException;
import com.example.domain.question.model.Question;
import com.example.domain.question.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository repository;

    public Question get(String id) {
        return _get(id);
    }

    private Question _get(String id) {
        return repository.findById(id).orElseThrow(QuestionException::notFound);
    }

    public Page<Question> findAll(Specification<Question> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }

    public Question create(String title, String description, String operatorId) {
        Question article = Question.builder()
                .title(title)
                .description(description)
                .createdBy(operatorId)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return repository.save(article);
    }

    public Question update(String id, String title, String description, String operatorId) {
        Question question = this._get(id);

        if (!question.getCreatedBy().equals(operatorId)) {
            throw QuestionException.forbidden();
        }

        question.setTitle(title);
        question.setDescription(description);
        question.setUpdatedAt(Instant.now());
        return repository.save(question);
    }

    public void delete(String id, String operatorId) {
        Optional<Question> optionalQuestion = repository.findById(id);
        if (!optionalQuestion.isPresent()) {
            return;
        }
        if (!optionalQuestion.get().getCreatedBy().equals(operatorId)) {
            throw QuestionException.forbidden();
        }
        repository.deleteById(id);
    }

}