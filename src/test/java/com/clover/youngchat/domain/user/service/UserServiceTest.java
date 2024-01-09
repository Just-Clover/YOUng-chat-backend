package com.clover.youngchat.domain.user.service;

import static com.clover.youngchat.global.exception.ResultCode.DUPLICATED_EMAIL;
import static com.clover.youngchat.global.exception.ResultCode.MISMATCH_CONFIRM_PASSWORD;
import static com.clover.youngchat.global.exception.ResultCode.MISMATCH_PASSWORD;
import static com.clover.youngchat.global.exception.ResultCode.SAME_OLD_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.clover.youngchat.domain.user.dto.request.UserSignupReq;
import com.clover.youngchat.domain.user.dto.request.UserUpdatePasswordReq;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import com.clover.youngchat.global.exception.GlobalException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import test.UserTest;

@ExtendWith(MockitoExtension.class)
class UserServiceTest implements UserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공테스트")
    void signupSuccessTest() {
        // given
        UserSignupReq req = UserSignupReq.builder()
            .email(TEST_USER_EMAIL)
            .username(TEST_USER_NAME)
            .password(TEST_USER_PASSWORD)
            .build();

        given(userRepository.existsByEmail(req.getEmail())).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(TEST_USER);

        // when
        userService.signup(req);

        // then
        verify(passwordEncoder, times(1)).encode(TEST_USER_PASSWORD);
        verify(userRepository, times(1)).existsByEmail(TEST_USER_EMAIL);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 실패테스트 : 중복된 이메일")
    void signupFailTest_duplicatedEmail() {
        // given
        UserSignupReq req = UserSignupReq.builder()
            .email(TEST_USER_EMAIL)
            .username(TEST_USER_NAME)
            .password(TEST_USER_PASSWORD)
            .build();

        given(userRepository.existsByEmail(req.getEmail())).willReturn(true);

        // when
        GlobalException exception =
            assertThrows(GlobalException.class, () -> userService.signup(req));

        // then
        assertThat(DUPLICATED_EMAIL.getMessage()).isEqualTo(exception.getResultCode().getMessage());
    }

    @Test
    @DisplayName("비밀번호 변경테스트 : 성공")
    void updatePasswordSuccessTest() {
        // given
        UserUpdatePasswordReq req = UserUpdatePasswordReq.builder()
            .prePassword(TEST_USER_PASSWORD)
            .newPassword(TEST_ANOTHER_USER_PASSWORD)
            .checkNewPassword(TEST_ANOTHER_USER_PASSWORD)
            .build();
        ReflectionTestUtils.setField(TEST_USER, "id", TEST_USER_ID);

        given(userRepository.findById(TEST_USER_ID)).willReturn(Optional.of(TEST_USER));
        given(passwordEncoder.matches(eq(TEST_USER_PASSWORD), any()))
            .willReturn(true);

        // when
        userService.updatePassword(TEST_USER_ID, req);

        // then
        verify(passwordEncoder, times(1)).encode(TEST_ANOTHER_USER_PASSWORD);
    }

    @Test
    @DisplayName("비밀번호 변경테스트 실패 : 기존비밀번호틀림")
    void updatePasswordFailTest() {
        // given
        UserUpdatePasswordReq req = UserUpdatePasswordReq.builder()
            .prePassword("WrongPassword")
            .newPassword(TEST_ANOTHER_USER_PASSWORD)
            .checkNewPassword(TEST_ANOTHER_USER_PASSWORD)
            .build();
        ReflectionTestUtils.setField(TEST_USER, "id", TEST_USER_ID);

        given(userRepository.findById(TEST_USER_ID)).willReturn(Optional.of(TEST_USER));
        given(passwordEncoder.matches(any(), any()))
            .willReturn(false);

        // when
        GlobalException exception =
            assertThrows(GlobalException.class,
                () -> userService.updatePassword(TEST_USER_ID, req));

        // then
        assertThat(MISMATCH_PASSWORD.getMessage()).isEqualTo(
            exception.getResultCode().getMessage());
    }

    @Test
    @DisplayName("비밀번호 변경테스트 실패 : 새로운비밀번호와 확인 비밀번호틀림")
    void updatePasswordFailTest2() {
        // given
        UserUpdatePasswordReq req = UserUpdatePasswordReq.builder()
            .prePassword(TEST_USER_PASSWORD)
            .newPassword(TEST_ANOTHER_USER_PASSWORD)
            .checkNewPassword("NotMatches")
            .build();
        ReflectionTestUtils.setField(TEST_USER, "id", TEST_USER_ID);

        given(userRepository.findById(TEST_USER_ID)).willReturn(Optional.of(TEST_USER));

        // when
        GlobalException exception =
            assertThrows(GlobalException.class,
                () -> userService.updatePassword(TEST_USER_ID, req));

        // then
        assertThat(MISMATCH_CONFIRM_PASSWORD.getMessage()).isEqualTo(
            exception.getResultCode().getMessage());
    }

    @Test
    @DisplayName("비밀번호 변경테스트 실패 : 이전 비밀번호와 같음")
    void updatePasswordFailTest3() {
        // given
        UserUpdatePasswordReq req = UserUpdatePasswordReq.builder()
            .prePassword(TEST_USER_PASSWORD)
            .newPassword(TEST_USER_PASSWORD)
            .checkNewPassword(TEST_USER_PASSWORD)
            .build();
        ReflectionTestUtils.setField(TEST_USER, "id", TEST_USER_ID);

        given(userRepository.findById(TEST_USER_ID)).willReturn(Optional.of(TEST_USER));
        given(passwordEncoder.matches(eq(TEST_USER_PASSWORD), any()))
            .willReturn(true);

        // when
        GlobalException exception =
            assertThrows(GlobalException.class,
                () -> userService.updatePassword(TEST_USER_ID, req));

        // then
        assertThat(SAME_OLD_PASSWORD.getMessage()).isEqualTo(
            exception.getResultCode().getMessage());
    }
}