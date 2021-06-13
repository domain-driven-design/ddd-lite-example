package com.example.domain.question.service;

import com.example.domain.question.exception.QuestionException;
import com.example.domain.question.model.Answer;
import com.example.domain.question.model.Question;
import com.example.domain.question.repository.AnswerRepository;
import com.example.domain.question.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository repository;
    @Autowired
    private AnswerRepository answerRepository;

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
        Question question = Question.builder()
                .title(title)
                .description(description)
                .createdBy(operatorId)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return repository.save(question);
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

        List<Answer> answers = answerRepository.findAll(Example.of(Answer.builder().questionId(id).build()));
        answerRepository.deleteAll(answers);
        repository.deleteById(id);
    }

    private Answer _getAnswer(String answerId) {
        return answerRepository.findById(answerId).orElseThrow(QuestionException::answerNotFound);
    }

    public Answer addAnswer(String id, String content, String operatorId) {
        Question question = _get(id);

        // TODO 业务验证：一个operator只能在一个question有一个answer

        Answer answer = Answer.builder()
                .questionId(id)
                .content(content)
                .createdBy(operatorId)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return answerRepository.save(answer);
    }

    public Page<Answer> findAllAnswers(Specification<Answer> spec, Pageable pageable) {
        return answerRepository.findAll(spec, pageable);
    }

    public Answer updateAnswer(String id, String answerId, String content, String operatorId) {
        Question question = _get(id);

        // TODO 业务验证 影响answer修改的question状态

        Answer answer = _getAnswer(answerId);
        if (!answer.getCreatedBy().equals(operatorId)) {
            throw QuestionException.answerForbidden();
        }

        answer.setContent(content);
        answer.setUpdatedAt(Instant.now());

        return answerRepository.save(answer);
    }

    public void deleteAnswer(String id, String answerId, String operatorId) {
        // TODO id 不需要

        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (!optionalAnswer.isPresent()) {
            return;
        }
        if (!optionalAnswer.get().getCreatedBy().equals(operatorId)) {
            throw QuestionException.answerForbidden();
        }
        answerRepository.deleteById(answerId);
    }
}
