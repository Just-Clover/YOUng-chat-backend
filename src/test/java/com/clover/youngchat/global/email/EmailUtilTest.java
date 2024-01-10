package com.clover.youngchat.global.email;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.clover.youngchat.domain.user.entity.EmailAuth;
import com.clover.youngchat.domain.user.service.EmailAuthService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring6.SpringTemplateEngine;
import test.EmailAuthTest;

@ExtendWith(MockitoExtension.class)
class EmailUtilTest implements EmailAuthTest {

    @InjectMocks
    EmailUtil emailUtil;
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private SpringTemplateEngine templateEngine;
    @Mock
    private EmailAuthService emailAuthService;

    @Test
    @DisplayName("인증코드 생성 테스트")
    public void createAuthCodeTest() {
        // when
        String code = emailUtil.createAuthCode();

        // then
        assertThat(code.length()).isEqualTo(8);
    }

    @Test
    @DisplayName("메시지 전송 테스트")
    public void sendMessageTest() {
        // given
        String subject = "YOUngChat! [이메일 인증]";
        MimeMessage message = mock(MimeMessage.class);

        given(emailAuthService.hasMail(TEST_EMAIL)).willReturn(true);
        given(javaMailSender.createMimeMessage()).willReturn(message);

        // when
        emailUtil.sendMessage(TEST_EMAIL, subject);

        // then
        verify(emailAuthService, times(1)).hasMail(TEST_EMAIL);
        verify(emailAuthService, times(1)).save(any(EmailAuth.class));
        verify(javaMailSender, times(1)).send(message);
    }

    @Test
    @DisplayName("인증코드 확인 테스트")
    public void checkCodeTest() {
        // given
        given(emailAuthService.findById(TEST_EMAIL)).willReturn(TEST_EMAIL_AUTH);

        // when
        emailUtil.checkCode(TEST_EMAIL, TEST_CODE);

        // then
        verify(emailAuthService, times(1)).updateAuthenticated(TEST_EMAIL_AUTH);
    }
}