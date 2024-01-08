package com.clover.youngchat.domain.user.dto.request;

import static com.clover.youngchat.domain.user.constant.UserConstant.EMAIL_REGEX;
import static com.clover.youngchat.domain.user.constant.UserConstant.PASSWORD_REGEX;
import static com.clover.youngchat.domain.user.constant.UserConstant.USERNAME_REGEX;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSignupReq {

    @Pattern(
        regexp = EMAIL_REGEX,
        message = "올바른 이메일 형식이 아닙니다."
    )
    private String email;

    @Pattern(
        regexp = USERNAME_REGEX,
        message = "username은 영소문자,한글 4글자 이상 10글자 이하입니다"
    )
    private String username;

    @Pattern(
        regexp = PASSWORD_REGEX,
        message = "비밀번호는 영소문자, 대문자, 특수문자, 숫자를 포함한 8글자 이상 15글자 이하입니다."
    )
    private String password;

    @Builder
    private UserSignupReq(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
