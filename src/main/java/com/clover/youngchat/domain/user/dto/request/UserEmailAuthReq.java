package com.clover.youngchat.domain.user.dto.request;

import static com.clover.youngchat.domain.user.constant.UserConstant.EMAIL_MESSAGE;
import static com.clover.youngchat.domain.user.constant.UserConstant.EMAIL_REGEX;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEmailAuthReq {

    @Pattern(regexp = EMAIL_REGEX, message = EMAIL_MESSAGE)
    private String email;
}
