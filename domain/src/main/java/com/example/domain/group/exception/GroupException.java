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

    public static GroupException memberConflict() {
        return new GroupException(Type.CONFLICT, "group_member_conflict");
    }

    public static GroupException ownerCanNotExit() {
        return new GroupException(Type.BAD_REQUEST, "group_owner_can_not_exit");
    }

    public static GroupException memberNotFound() {
        return new GroupException(Type.NOT_FOUND, "group_member_not_found");
    }

    public static GroupException ownerCanNotChange() {
        return new GroupException(Type.BAD_REQUEST, "group_member_not_change");
    }
}
