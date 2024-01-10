package com.clover.youngchat.global.email;

import com.clover.youngchat.domain.user.entity.EmailAuth;
import com.clover.youngchat.domain.user.service.EmailAuthService;
import com.clover.youngchat.global.exception.GlobalException;
import com.clover.youngchat.global.exception.ResultCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j(topic = "email 인증코드 생성 및 발송")
@Component
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final EmailAuthService emailAuthService;

    @Value("${spring.mail.username}")
    private String email;

    public String createAuthCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public void sendMessage(String to, String subject) {
        try {
            String code = createAuthCode();
            MimeMessage message = createMessage(to, subject, code);

            if (emailAuthService.hasMail(to)) {
                emailAuthService.delete(to);
            }
            EmailAuth emailAuth = EmailAuth.builder()
                .email(to)
                .code(code)
                .build();

            emailAuthService.save(emailAuth);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new GlobalException(ResultCode.EMAIL_SEND_FAIL);
        }
    }

    public boolean checkCode(String to, String code) {

        EmailAuth emailAuth = emailAuthService.findById(to);

        return emailAuth.getCode().equals(code);
    }

    private MimeMessage createMessage(String to, String subject, String code)
        throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(email);
        message.addRecipients(RecipientType.TO, to);
        message.setSubject(subject, StandardCharsets.UTF_8.name());
        message.setText(createMailHtml(code), "UTF-8", "html");

        return message;
    }

    private String createMailHtml(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("email", context);
    }

}
