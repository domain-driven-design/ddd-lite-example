package com.example.domain.question.exception;

import com.example.domain.common.BaseException;

public class QuestionException extends BaseException {
    public QuestionException(Type type, String message) {
        super(type, message);
    }

    public static QuestionException notFound() {
        return new QuestionException(Type.NOT_FOUND, "question_not_found");
    }

    public static QuestionException forbidden() {
        return new QuestionException(Type.FORBIDDEN, "question_operation_forbidden");
    }

    public static QuestionException answerNotFound() {
        return new QuestionException(Type.NOT_FOUND, "answer_not_found");
    }

    public static QuestionException answerForbidden() {
        return new QuestionException(Type.FORBIDDEN, "answer_operation_forbidden");

    }
}
