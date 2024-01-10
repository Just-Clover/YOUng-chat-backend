package com.clover.youngchat.domain.user.service;

import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.clover.youngchat.domain.user.repository.EmailAuthRepository;
import com.clover.youngchat.global.exception.GlobalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.EmailAuthTest;

@ExtendWith(MockitoExtension.class)
class EmailAuthServiceTest implements EmailAuthTest {

    @Mock
    private EmailAuthRepository emailAuthRepository;

    @InjectMocks
    private EmailAuthService emailAuthService;

    @Test
    @DisplayName("인증상태 업데이트 테스트")
    public void updateAuthenticatedTest() {
        // when
        emailAuthService.updateAuthenticated(TEST_EMAIL_AUTH);

        // then
        verify(emailAuthRepository, times(1)).save(TEST_EMAIL_AUTH);
        assertThat(TEST_EMAIL_AUTH.isAuthenticated()).isTrue();
    }

    @Test
    @DisplayName("이메일 찾기 실패 테스트")
    public void findByIdTest() {

        // when
        GlobalException exception =
            assertThrows(GlobalException.class, () -> emailAuthService.findById(TEST_EMAIL));

        // then
        assertThat(NOT_FOUND_EMAIL.getMessage()).isEqualTo(exception.getResultCode().getMessage());
    }

}