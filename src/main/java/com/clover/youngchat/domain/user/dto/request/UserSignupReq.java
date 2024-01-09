package com.clover.youngchat.domain.user.dto.request;

import static com.clover.youngchat.domain.user.constant.UserConstant.EMAIL_MESSAGE;
import static com.clover.youngchat.domain.user.constant.UserConstant.EMAIL_REGEX;
import static com.clover.youngchat.domain.user.constant.UserConstant.PASSWORD_MESSAGE;
import static com.clover.youngchat.domain.user.constant.UserConstant.PASSWORD_REGEX;
import static com.clover.youngchat.domain.user.constant.UserConstant.USERNAME_MESSAGE;
import static com.clover.youngchat.domain.user.constant.UserConstant.USERNAME_REGEX;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSignupReq {

    @Pattern(regexp = EMAIL_REGEX, message = EMAIL_MESSAGE)
    private String email;

    @Pattern(regexp = USERNAME_REGEX, message = USERNAME_MESSAGE)
    private String username;

    @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_MESSAGE)
    private String password;

    @Builder
    private UserSignupReq(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
