package com.example.domain.user.service;

import com.example.domain.common.BaseException;
import com.example.domain.group.exception.GroupException;
import com.example.domain.user.exception.UserException;
import com.example.domain.user.model.User;
import com.example.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void should_throw_frozen_when_get_user_as_frozen() {
        // given
        Mockito.when(userRepository.findById(eq("test-user-id")))
                .thenReturn(Optional.of(User.builder().id("test-user-id").status(User.Status.FROZEN).build()));
        // when
        BaseException exception = assertThrows(UserException.class, () -> {
            //when
            userService.get("test-user-id");
        });

        assertEquals("user_frozen", exception.getMessage());
        assertEquals(BaseException.Type.BAD_REQUEST, exception.getType());
    }

}