package com.example.domain.auth.model;

import com.example.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authorize {
    private String id;
    private String userId;
    private User.Role role;
    private Long expire;
}
