package com.clover.youngchat.domain.user.service;

import com.clover.youngchat.domain.user.entity.EmailAuth;
import com.clover.youngchat.domain.user.repository.EmailAuthRepository;
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

    public EmailAuth save(final EmailAuth emailAuth) {
        return emailAuthRepository.save(emailAuth);
    }

    public EmailAuth findById(final String email) {
        return emailAuthRepository.findById(email)
            .orElseThrow(() -> new GlobalException(ResultCode.NOT_FOUND_EMAIL));
    }
}