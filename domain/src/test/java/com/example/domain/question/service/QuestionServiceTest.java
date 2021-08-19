package com.example.domain.question.service;

import com.example.domain.common.BaseException;
import com.example.domain.group.exception.GroupException;
import com.example.domain.group.model.GroupMember;
import com.example.domain.group.repository.GroupMemberRepository;
import com.example.domain.group.repository.GroupRepository;
import com.example.domain.group.service.GroupService;
import com.example.domain.question.exception.QuestionException;
import com.example.domain.question.model.Question;
import com.example.domain.question.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {
    @InjectMocks
    private QuestionService questionService;

    @Mock
    private QuestionRepository questionRepository;

    @Test
    void should_throw_exception_when_role_is_normal_and_not_creator() {
        Mockito.when(questionRepository.findById(eq("test-id")))
                .thenReturn(Optional.of(Question.builder().id("test-id").createdBy("test-user-id").build()));

        BaseException exception = assertThrows(QuestionException.class, () -> {
            questionService.delete(
                    "test-id", GroupMember.builder().role(GroupMember.Role.NORMAL).userId("test-other-user-id").build()
            );
        });

        assertEquals("question_operation_forbidden", exception.getMessage());
        assertEquals(BaseException.Type.FORBIDDEN, exception.getType());
    }
}