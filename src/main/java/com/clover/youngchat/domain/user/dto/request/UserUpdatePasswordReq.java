package com.clover.youngchat.domain.user.dto.request;

import static com.clover.youngchat.domain.user.constant.UserConstant.PASSWORD_MESSAGE;
import static com.clover.youngchat.domain.user.constant.UserConstant.PASSWORD_REGEX;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUpdatePasswordReq {

    private String prePassword;

    @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_MESSAGE)
    private String newPassword;

    private String checkNewPassword;

    @Builder
    private UserUpdatePasswordReq(String prePassword, String newPassword, String checkNewPassword) {
        this.prePassword = prePassword;
        this.newPassword = newPassword;
        this.checkNewPassword = checkNewPassword;
    }
}
