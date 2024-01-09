package com.clover.youngchat.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileEditReq {

    private String username;
    private String profileImage;

    @Builder
    private UserProfileEditReq(String username, String profileImage) {
        this.username = username;
        this.profileImage = profileImage;
    }
}
