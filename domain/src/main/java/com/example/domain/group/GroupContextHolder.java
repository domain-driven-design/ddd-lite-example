package com.example.domain.group;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GroupContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static String getContext() {
        return contextHolder.get();
    }

    public static void setContext(String context) {
        contextHolder.set(context);
    }

}
