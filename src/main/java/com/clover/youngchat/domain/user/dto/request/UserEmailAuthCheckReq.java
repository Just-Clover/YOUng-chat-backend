package com.clover.youngchat.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEmailAuthCheckReq {

    String email;
    String code;

    @Builder
    private UserEmailAuthCheckReq(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
