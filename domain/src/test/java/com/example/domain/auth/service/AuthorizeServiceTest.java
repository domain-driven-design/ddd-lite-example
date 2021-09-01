package com.example.domain.auth.service;

import com.example.domain.auth.AuthorizeContextHolder;
import com.example.domain.auth.exception.AuthorizeException;
import com.example.domain.auth.model.Authorize;
import com.example.domain.common.BaseException;
import com.example.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class AuthorizeServiceTest {

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private AuthorizeService authorizeService;

    @Test
    void should_create_authorize_failed_when_password_is_wrong() {
        //given
        User user = User.builder()
                .password("testPassword")
                .id("test-user-id")
                .role(User.Role.ADMIN)
                .build();
        String password = "wrongTestPassword";
        Mockito.when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);

        BaseException exception = assertThrows(AuthorizeException.class, () -> {
            //when
            authorizeService.create(user, password);
        });

        assertEquals("invalid_credential", exception.getMessage());
        assertEquals(BaseException.Type.UNAUTHORIZED, exception.getType());
    }

    @Test
    void should_fetch_authorize_when_authorize_exist() {
        //given
        AuthorizeContextHolder.setContext(
                Authorize.builder().id("test-authorize").userId("test-user-id").build()
        );
        //when
        Authorize current = authorizeService.getCurrent();
        //then
        assertEquals("test-authorize", current.getId());
        AuthorizeContextHolder.setContext(null);
    }

    @Test
    void should_fetch_authorize_failed_when_authorize_is_not_exist() {
        //when
        BaseException exception = assertThrows(AuthorizeException.class, () -> {
            //when
            authorizeService.getOperator();
        });

        assertEquals("unauthorized", exception.getMessage());
        assertEquals(BaseException.Type.UNAUTHORIZED, exception.getType());
    }

}
