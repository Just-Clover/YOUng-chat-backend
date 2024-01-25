package com.clover.youngchat.domain.auth.service;

import com.clover.youngchat.domain.auth.entity.EmailAuth;
import com.clover.youngchat.domain.auth.repository.EmailAuthRepository;
import com.clover.youngchat.global.exception.GlobalException;
import com.clover.youngchat.global.exception.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final EmailAuthRepository emailAuthRepository;

    public boolean hasMail(final String email) {
        return emailAuthRepository.existsById(email);
    }

    public void delete(final String email) {
        emailAuthRepository.deleteById(email);
    }

    public void updateAuthenticated(final EmailAuth emailAuth) {
        emailAuth.updateAuthenticated(true);
        save(emailAuth);
    }

    public EmailAuth save(final EmailAuth emailAuth) {
        return emailAuthRepository.save(emailAuth);
    }

    public EmailAuth findById(final String email) {
        return emailAuthRepository.findById(email)
            .orElseThrow(() -> new GlobalException(ResultCode.NOT_FOUND_EMAIL));
    }
}