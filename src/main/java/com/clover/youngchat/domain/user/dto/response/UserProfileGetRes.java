package com.clover.youngchat.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileGetRes {

    private String username;
    private String profileImage;

    @Builder
    private UserProfileGetRes(String username, String profileImage) {
        this.username = username;
        this.profileImage = profileImage;
    }
}
