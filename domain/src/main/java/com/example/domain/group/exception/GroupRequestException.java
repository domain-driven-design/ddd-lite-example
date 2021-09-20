package com.example.domain.group.exception;

import com.example.domain.common.BaseException;

public class GroupRequestException extends BaseException {
    public GroupRequestException(Type type, String message) {
        super(type, message);
    }

    public static GroupRequestException notFound() {
        return new GroupRequestException(Type.NOT_FOUND, "group_not_found");
    }
}
