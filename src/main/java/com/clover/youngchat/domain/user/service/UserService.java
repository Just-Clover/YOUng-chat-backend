package com.clover.youngchat.domain.user.service;

import static com.clover.youngchat.global.exception.ResultCode.ACCESS_DENY;
import static com.clover.youngchat.global.exception.ResultCode.DUPLICATED_EMAIL;
import static com.clover.youngchat.global.exception.ResultCode.MISMATCH_CONFIRM_PASSWORD;
import static com.clover.youngchat.global.exception.ResultCode.MISMATCH_PASSWORD;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_USER;
import static com.clover.youngchat.global.exception.ResultCode.SAME_OLD_PASSWORD;

import com.clover.youngchat.domain.user.dto.request.UserProfileEditReq;
import com.clover.youngchat.domain.user.dto.request.UserSignupReq;
import com.clover.youngchat.domain.user.dto.request.UserUpdatePasswordReq;
import com.clover.youngchat.domain.user.dto.response.UserProfileEditRes;
import com.clover.youngchat.domain.user.dto.response.UserProfileGetRes;
import com.clover.youngchat.domain.user.dto.response.UserSignupRes;
import com.clover.youngchat.domain.user.dto.response.UserUpdatePasswordRes;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import com.clover.youngchat.global.exception.GlobalException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public UserUpdatePasswordRes updatePassword(Long userId, UserUpdatePasswordReq req) {
        User foundUser = findUserById(userId);

        validatePasswords(req, foundUser);

        String encryptedPassword = passwordEncoder.encode(req.getNewPassword());
        foundUser.updatePassword(encryptedPassword);

        return new UserUpdatePasswordRes();
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_USER));
    }

    private void validateSignup(UserSignupReq userSignupReq) {
        if (userRepository.existsByEmail(userSignupReq.getEmail())) {
            throw new GlobalException(DUPLICATED_EMAIL);
        }
    }

    private void validatePasswords(UserUpdatePasswordReq req, User foundUser) {
        if (!req.getNewPassword().equals(req.getCheckNewPassword())) {
            throw new GlobalException(MISMATCH_CONFIRM_PASSWORD);
        }

        if (!passwordEncoder.matches(req.getPrePassword(), foundUser.getPassword())) {
            throw new GlobalException(MISMATCH_PASSWORD);
        }

        if (req.getPrePassword().equals(req.getNewPassword())) {
            throw new GlobalException(SAME_OLD_PASSWORD);
        }
    }
}
