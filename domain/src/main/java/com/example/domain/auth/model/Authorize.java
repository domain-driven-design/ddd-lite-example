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
// TODO  把用户的基本信信息也写入这个对象
public class Authorize {
    private String id;
    private String userId;
    private User.UserRole role;
    private Long expire;
}
