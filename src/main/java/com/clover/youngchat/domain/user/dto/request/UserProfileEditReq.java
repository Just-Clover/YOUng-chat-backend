package com.clover.youngchat.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileEditReq {

    private String username;

    @Builder
    private UserProfileEditReq(String username) {
        this.username = username;
    }
}
