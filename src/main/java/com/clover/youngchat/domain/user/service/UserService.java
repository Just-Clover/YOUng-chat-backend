package com.clover.youngchat.domain.user.service;

import static com.clover.youngchat.global.exception.ResultCode.DUPLICATED_EMAIL;

import com.clover.youngchat.domain.user.dto.request.UserSignupReq;
import com.clover.youngchat.domain.user.dto.response.UserSignupRes;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import com.clover.youngchat.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSignupRes signup(UserSignupReq userSignupReq) {

        validateSignup(userSignupReq);
        String encryptionPassword = passwordEncoder.encode(userSignupReq.getPassword());

        User user = User.builder()
            .email(userSignupReq.getEmail())
            .username(userSignupReq.getUsername())
            .password(userSignupReq.getPassword())
            .build();

        userRepository.save(user);

        return new UserSignupRes();
    }

    private void validateSignup(UserSignupReq userSignupReq) {
        if (userRepository.existsByEmail(userSignupReq.getEmail())) {
            throw new GlobalException(DUPLICATED_EMAIL);
        }
    }
}
