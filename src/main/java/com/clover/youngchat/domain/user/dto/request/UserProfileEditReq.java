package com.clover.youngchat.domain.user.dto.request;

import static com.clover.youngchat.domain.user.constant.UserConstant.USERNAME_MESSAGE;
import static com.clover.youngchat.domain.user.constant.UserConstant.USERNAME_REGEX;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileEditReq {

    @Pattern(regexp = USERNAME_REGEX, message = USERNAME_MESSAGE)
    private String username;

    @Builder
    private UserProfileEditReq(String username) {
        this.username = username;
    }
}
