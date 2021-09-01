package com.example.domain.common.config;


import com.example.domain.auth.AuthorizeContextHolder;
import com.example.domain.auth.exception.AuthorizeException;
import com.example.domain.auth.model.Authorize;
import com.example.domain.auth.repository.AuthorizeRepository;
import com.example.domain.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Component
public class AuthorizeInterceptor implements HandlerInterceptor {
    @Autowired
    private AuthorizeRepository authorizeRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");

        Authorize authorize = null;
        if (!isBlank(token)) {
            authorize = authorizeRepository.get(trimBear(token));
        }

        AuthorizeContextHolder.setContext(authorize);

        if (request.getRequestURI().startsWith("/management")) {
            if (authorize == null || !User.Role.ADMIN.equals(authorize.getRole())) {
                throw AuthorizeException.Unauthorized();
            }
        }

        return true;
    }

    private String trimBear(String tokenString) {
        return StringUtils.remove(tokenString, "Bearer ");
    }

}
