package com.clover.youngchat.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileGetRes {

    private Long userId;
    private String username;
    private String profileImage;
    private String email;

    @Builder
    private UserProfileGetRes(Long userId, String username, String profileImage, String email) {
        this.userId = userId;
        this.username = username;
        this.profileImage = profileImage;
        this.email = email;
    }
}
