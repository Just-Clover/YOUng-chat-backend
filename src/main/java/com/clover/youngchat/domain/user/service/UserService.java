package com.clover.youngchat.domain.user.service;

import static com.clover.youngchat.global.exception.ResultCode.ACCESS_DENY;
import static com.clover.youngchat.global.exception.ResultCode.DUPLICATED_EMAIL;
import static com.clover.youngchat.global.exception.ResultCode.INVALID_PROFILE_IMAGE_TYPE;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_FILE;
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
import com.clover.youngchat.global.s3.S3Util;
import com.clover.youngchat.global.s3.S3Util.FilePath;
import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Util s3Util;

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
    public UserProfileEditRes editProfile(Long userId, UserProfileEditReq req,
        MultipartFile multipartFile,
        Long authUserId) {
        // 수정하고자 하는 프로필이 본인의 프로필이 아닌 경우
        if (!userId.equals(authUserId)) {
            throw new GlobalException(ACCESS_DENY);
        } else if (multipartFile == null) {
            throw new GlobalException(NOT_FOUND_FILE);
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_USER));

        String profileImageUrl = user.getProfileImage();

        if (Objects.equals(multipartFile.getContentType(), "image/png")) {
            profileImageUrl = s3Util.uploadFile(multipartFile, FilePath.PROFILE);
        } else {
            throw new GlobalException(INVALID_PROFILE_IMAGE_TYPE);
        }

        user.updateProfile(req, profileImageUrl);

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
