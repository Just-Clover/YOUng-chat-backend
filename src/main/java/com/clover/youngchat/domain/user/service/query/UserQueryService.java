package com.clover.youngchat.domain.user.service.query;

import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_EMAIL;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_USER;

import com.clover.youngchat.domain.user.dto.response.UserEmailCheckRes;
import com.clover.youngchat.domain.user.dto.response.UserProfileGetRes;
import com.clover.youngchat.domain.user.dto.response.UserProfileSearchRes;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import com.clover.youngchat.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;

    public UserProfileGetRes getProfile(Long userId, User user) {
        String email = null;

        if (userId != null) {
            user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(NOT_FOUND_USER));
        } else {
            email = user.getEmail();
        }

        return UserProfileGetRes.to(user, email);
    }

    public UserProfileSearchRes searchProfile(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_EMAIL));

        return UserProfileSearchRes.to(user);
    }

    public UserEmailCheckRes checkEmailDuplicated(final String email) {
        return UserEmailCheckRes.to(userRepository.existsByEmail(email));
    }

}
