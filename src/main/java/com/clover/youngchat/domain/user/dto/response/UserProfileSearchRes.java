package com.clover.youngchat.domain.user.dto.response;

import com.clover.youngchat.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileSearchRes {

    private Long userId;
    private String email;
    private String username;
    private String profileImage;

    @Builder
    private UserProfileSearchRes(Long userId, String email, String username, String profileImage) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.profileImage = profileImage;
    }

    public static UserProfileSearchRes to(User user) {
        return UserProfileSearchRes.builder()
            .userId(user.getId())
            .email(user.getEmail())
            .username(user.getUsername())
            .profileImage(user.getProfileImage())
            .build();
    }
}

