package com.clover.youngchat.domain.user.controller.valid;

import static com.clover.youngchat.domain.user.constant.UserConstant.EMAIL_REGEX;
import static com.clover.youngchat.domain.user.constant.UserConstant.PASSWORD_REGEX;
import static com.clover.youngchat.domain.user.constant.UserConstant.USERNAME_REGEX;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Pattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.UserTest;

public class UserValidTest implements UserTest {

    @Test
    @DisplayName("이메일 유효성 검사")
    void validateEmailPattern() {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);

        assertThat(pattern.matcher(TEST_USER_EMAIL).matches()).isTrue();
        assertThat(pattern.matcher("invalidEmail").matches()).isFalse();
    }

    @Test
    @DisplayName("이름 유효성 검사")
    void validateUsernamePattern() {
        Pattern pattern = Pattern.compile(USERNAME_REGEX);

        assertThat(pattern.matcher(TEST_USER_NAME).matches()).isTrue();
        assertThat(pattern.matcher("in").matches()).isFalse();
    }

    @Test
    @DisplayName("비밀번호 유효성 검사")
    void validatePasswordPattern() {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);

        assertThat(pattern.matcher(TEST_USER_PASSWORD).matches()).isTrue();
        assertThat(pattern.matcher("short").matches()).isFalse();
    }

}
