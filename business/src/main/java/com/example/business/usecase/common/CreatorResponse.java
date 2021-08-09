package com.example.business.usecase.common;

import com.example.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreatorResponse {
    String id;
    String name;

    public static CreatorResponse from(User user) {
        if (user == null) {
            return null;
        }
        return CreatorResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

}

