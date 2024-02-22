package com.clover.youngchat.domain.user.service.command;

import static com.clover.youngchat.domain.user.constant.UserConstant.EMAIL_AUTHENTICATION;
import static com.clover.youngchat.global.exception.ResultCode.ACCESS_DENY;
import static com.clover.youngchat.global.exception.ResultCode.DUPLICATED_EMAIL;
import static com.clover.youngchat.global.exception.ResultCode.MISMATCH_CONFIRM_PASSWORD;
import static com.clover.youngchat.global.exception.ResultCode.MISMATCH_PASSWORD;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_USER;
import static com.clover.youngchat.global.exception.ResultCode.SAME_OLD_PASSWORD;
import static com.clover.youngchat.global.exception.ResultCode.UNAUTHORIZED_EMAIL;

import com.clover.youngchat.domain.auth.entity.EmailAuth;
import com.clover.youngchat.domain.user.dto.request.UserEmailAuthCheckReq;
import com.clover.youngchat.domain.user.dto.request.UserEmailAuthReq;
import com.clover.youngchat.domain.user.dto.request.UserProfileEditReq;
import com.clover.youngchat.domain.user.dto.request.UserSignupReq;
import com.clover.youngchat.domain.user.dto.request.UserUpdatePasswordReq;
import com.clover.youngchat.domain.user.dto.response.UserEmailAuthCheckRes;
import com.clover.youngchat.domain.user.dto.response.UserEmailAuthRes;
import com.clover.youngchat.domain.user.dto.response.UserProfileEditRes;
import com.clover.youngchat.domain.user.dto.response.UserSignupRes;
import com.clover.youngchat.domain.user.dto.response.UserUpdatePasswordRes;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import com.clover.youngchat.global.email.EmailUtil;
import com.clover.youngchat.global.exception.GlobalException;
import com.clover.youngchat.global.s3.S3Util;
import com.clover.youngchat.global.s3.S3Util.FilePath;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Util s3Util;
    private final EmailUtil emailUtil;

    @Value("${default.image.url}")
    private String defaultProfileImageUrl;

    private static void validateUserId(Long userId, Long authUserId) {
        if (!userId.equals(authUserId)) {
            throw new GlobalException(ACCESS_DENY);
        }
    }

    public UserSignupRes signup(UserSignupReq userSignupReq) {

        validateSignup(userSignupReq);
        String encryptionPassword = passwordEncoder.encode(userSignupReq.getPassword());

        User user = User.builder()
            .email(userSignupReq.getEmail())
            .username(userSignupReq.getUsername())
            .password(encryptionPassword)
            .profileImage(defaultProfileImageUrl)
            .build();

        userRepository.save(user);

        return new UserSignupRes();
    }

    public UserProfileEditRes editProfile(Long userId, UserProfileEditReq req,
        MultipartFile multipartFile,
        Long authUserId) {

        validateUserId(userId, authUserId);

        User user = findUserById(userId);

        String profileImageUrl = user.getProfileImage();

        if (multipartFile != null && !multipartFile.isEmpty()) {
            profileImageUrl = updateProfileImage(multipartFile, profileImageUrl);
        }

        if (!user.getProfileImage().equals(profileImageUrl)) {
            user.updateProfile(req, profileImageUrl);
        }

        return new UserProfileEditRes();
    }

    public UserUpdatePasswordRes updatePassword(Long userId, UserUpdatePasswordReq req) {
        User foundUser = findUserById(userId);

        validatePasswords(req, foundUser);

        String encryptedPassword = passwordEncoder.encode(req.getNewPassword());
        foundUser.updatePassword(encryptedPassword);

        return new UserUpdatePasswordRes();
    }

    public UserEmailAuthRes sendAuthEmail(final UserEmailAuthReq req) {
        emailUtil.sendMessage(req.getEmail(), EMAIL_AUTHENTICATION);
        return new UserEmailAuthRes();
    }

    public UserEmailAuthCheckRes checkAuthEmail(final UserEmailAuthCheckReq req) {
        emailUtil.checkCode(req.getEmail(), req.getCode());
        return new UserEmailAuthCheckRes();
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_USER));
    }

    private void validateSignup(UserSignupReq req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new GlobalException(DUPLICATED_EMAIL);
        }

        EmailAuth emailAuth = emailUtil.findEmailAuth(req.getEmail());
        if (!emailAuth.isAuthenticated()) {
            throw new GlobalException(UNAUTHORIZED_EMAIL);
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

    private String updateProfileImage(MultipartFile multipartFile, String oldImageUrl) {
        String newImageUrl = s3Util.uploadFile(multipartFile, FilePath.PROFILE);
        if (newImageUrl != null && !newImageUrl.equals(oldImageUrl)) {
            deleteProfileImage(oldImageUrl);
            return newImageUrl;
        }
        return oldImageUrl;
    }

    private void deleteProfileImage(String imageUrl) {
        if (!imageUrl.equals(defaultProfileImageUrl)) {
            s3Util.deleteFile(imageUrl, FilePath.PROFILE);
        }
    }
}
