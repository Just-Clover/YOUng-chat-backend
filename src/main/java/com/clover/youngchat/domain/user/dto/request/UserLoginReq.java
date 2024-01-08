package com.clover.youngchat.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginReq {

    // TODO:: constant pattern 미반영
    private String email;
    // TODO:: constant pattern 미반영
    private String password;

    @Builder
    private UserLoginReq(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
