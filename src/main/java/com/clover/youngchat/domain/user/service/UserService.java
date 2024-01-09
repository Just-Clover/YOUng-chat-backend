package com.clover.youngchat.domain.user.service;

import static com.clover.youngchat.global.exception.ResultCode.ACCESS_DENY;
import static com.clover.youngchat.global.exception.ResultCode.DUPLICATED_EMAIL;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_USER;

import com.clover.youngchat.domain.user.dto.request.UserProfileEditReq;
import com.clover.youngchat.domain.user.dto.request.UserSignupReq;
import com.clover.youngchat.domain.user.dto.response.UserProfileEditRes;
import com.clover.youngchat.domain.user.dto.response.UserProfileGetRes;
import com.clover.youngchat.domain.user.dto.response.UserSignupRes;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import com.clover.youngchat.global.exception.GlobalException;
import jakarta.transaction.Transactional;
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
            .password(encryptionPassword)
            .build();

        userRepository.save(user);

        return new UserSignupRes();
    }

    public UserProfileGetRes getProfile(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_USER));

        return UserProfileGetRes.builder()
            .username(user.getUsername())
            .profileImage(user.getProfileImage())
            .build();
    }

    private void validateSignup(UserSignupReq userSignupReq) {
        if (userRepository.existsByEmail(userSignupReq.getEmail())) {
            throw new GlobalException(DUPLICATED_EMAIL);
        }
    }

    @Transactional
    public UserProfileEditRes editProfile(Long userId, UserProfileEditReq req, Long authUserId) {
        // 수정하고자 하는 프로필이 본인의 프로필이 아닌 경우
        if (!userId.equals(authUserId)) {
            throw new GlobalException(ACCESS_DENY);
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_USER));

        user.updateProfile(req);

        return new UserProfileEditRes();
    }
}
