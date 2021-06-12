package com.example.domain.question.repository;

import com.example.domain.question.model.Answer;
import com.example.domain.question.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, String>, JpaSpecificationExecutor<Answer> {
}
