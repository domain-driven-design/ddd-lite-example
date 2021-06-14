package com.example.domain.group.exception;

import com.example.domain.common.BaseException;

public class GroupException extends BaseException {
    public GroupException(Type type, String message) {
        super(type, message);
    }

    public static GroupException notFound() {
        return new GroupException(Type.NOT_FOUND, "group_not_found");
    }

    public static GroupException forbidden() {
        return new GroupException(Type.FORBIDDEN, "group_operation_forbidden");
    }
}
