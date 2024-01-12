package com.clover.youngchat.domain.user.service;

import static com.clover.youngchat.global.exception.ResultCode.ACCESS_DENY;
import static com.clover.youngchat.global.exception.ResultCode.DUPLICATED_EMAIL;
import static com.clover.youngchat.global.exception.ResultCode.INVALID_PROFILE_IMAGE_TYPE;
import static com.clover.youngchat.global.exception.ResultCode.MISMATCH_CONFIRM_PASSWORD;
import static com.clover.youngchat.global.exception.ResultCode.MISMATCH_PASSWORD;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_USER;
import static com.clover.youngchat.global.exception.ResultCode.SAME_OLD_PASSWORD;
import static com.clover.youngchat.global.exception.ResultCode.UNAUTHORIZED_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.IMAGE_GIF_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import com.clover.youngchat.domain.user.dto.request.UserProfileEditReq;
import com.clover.youngchat.domain.user.dto.request.UserSignupReq;
import com.clover.youngchat.domain.user.dto.request.UserUpdatePasswordReq;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import com.clover.youngchat.global.email.EmailUtil;
import com.clover.youngchat.global.exception.GlobalException;
import com.clover.youngchat.global.s3.S3Util;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import test.EmailAuthTest;
import test.UserTest;

@ExtendWith(MockitoExtension.class)
class UserServiceTest implements UserTest, EmailAuthTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailUtil emailUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private S3Util s3Util;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .email(TEST_USER_EMAIL)
            .username(TEST_USER_NAME)
            .password(TEST_USER_PASSWORD)
            .profileImage(TEST_USER_PROFILE_IMAGE)
            .build();

        ReflectionTestUtils.setField(testUser, "id", 1L);
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class signupTest {

        UserSignupReq req;

        @BeforeEach
        void setup() {
            req = UserSignupReq.builder()
                .email(TEST_USER_EMAIL)
                .username(TEST_USER_NAME)
                .password(TEST_USER_PASSWORD)
                .build();
        }

        @Test
        @DisplayName("성공")
        void signupSuccessTest() {
            // given
            given(userRepository.existsByEmail(req.getEmail())).willReturn(false);
            given(emailUtil.findEmailAuth(req.getEmail())).willReturn(TEST_EMAIL_AUTH_OK);
            given(userRepository.save(any(User.class))).willReturn(testUser);

            // when
            userService.signup(req);

            // then
            verify(passwordEncoder, times(1)).encode(TEST_USER_PASSWORD);
            verify(userRepository, times(1)).existsByEmail(TEST_USER_EMAIL);
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("실패 : 중복된 이메일")
        void signupFailTest_duplicatedEmail() {
            // given
            given(userRepository.existsByEmail(req.getEmail())).willReturn(true);

            // when
            GlobalException exception = assertThrows(GlobalException.class,
                () -> userService.signup(req));

            // then
            assertThat(DUPLICATED_EMAIL.getMessage()).isEqualTo(
                exception.getResultCode().getMessage());
        }

        @Test
        @DisplayName("실패 : 인증되지 않은 이메일")
        void signupFailTest_invalidEmail() {
            // given
            given(userRepository.existsByEmail(req.getEmail())).willReturn(false);
            given(emailUtil.findEmailAuth(req.getEmail())).willReturn(TEST_EMAIL_AUTH_FAIL);

            // when
            GlobalException exception = assertThrows(GlobalException.class,
                () -> userService.signup(req));

            // then
            assertThat(exception.getResultCode().getMessage())
                .isEqualTo(UNAUTHORIZED_EMAIL.getMessage())
            ;
        }

    }

    @Nested
    @DisplayName("비밀번호 변경 테스트")
    class updatePasswordTest {

        @Test
        @DisplayName("성공")
        void updatePasswordSuccessTest() {
            // given
            UserUpdatePasswordReq req = UserUpdatePasswordReq.builder()
                .prePassword(TEST_USER_PASSWORD)
                .newPassword(TEST_ANOTHER_USER_PASSWORD)
                .checkNewPassword(TEST_ANOTHER_USER_PASSWORD)
                .build();
            ReflectionTestUtils.setField(TEST_USER, "id", TEST_USER_ID);

            given(userRepository.findById(TEST_USER_ID)).willReturn(Optional.of(testUser));
            given(passwordEncoder.matches(eq(TEST_USER_PASSWORD), any())).willReturn(true);

            // when
            userService.updatePassword(TEST_USER_ID, req);

            // then
            verify(passwordEncoder, times(1)).encode(TEST_ANOTHER_USER_PASSWORD);
        }

        @Test
        @DisplayName("실패 : 기존 비밀번호 틀림")
        void updatePasswordFailTest() {
            // given
            UserUpdatePasswordReq req = UserUpdatePasswordReq.builder()
                .prePassword("WrongPassword")
                .newPassword(TEST_ANOTHER_USER_PASSWORD)
                .checkNewPassword(TEST_ANOTHER_USER_PASSWORD)
                .build();
            ReflectionTestUtils.setField(testUser, "id", TEST_USER_ID);

            given(userRepository.findById(TEST_USER_ID)).willReturn(Optional.of(testUser));
            given(passwordEncoder.matches(any(), any())).willReturn(false);

            // when
            GlobalException exception = assertThrows(GlobalException.class,
                () -> userService.updatePassword(TEST_USER_ID, req));

            // then
            assertThat(MISMATCH_PASSWORD.getMessage()).isEqualTo(
                exception.getResultCode().getMessage());
        }

        @Test
        @DisplayName("실패 : 새로운비밀번호와 확인 비밀번호 틀림")
        void updatePasswordFailTest2() {
            // given
            UserUpdatePasswordReq req = UserUpdatePasswordReq.builder()
                .prePassword(TEST_USER_PASSWORD)
                .newPassword(TEST_ANOTHER_USER_PASSWORD)
                .checkNewPassword("NotMatches")
                .build();
            ReflectionTestUtils.setField(testUser, "id", TEST_USER_ID);

            given(userRepository.findById(TEST_USER_ID)).willReturn(Optional.of(testUser));

            // when
            GlobalException exception = assertThrows(GlobalException.class,
                () -> userService.updatePassword(TEST_USER_ID, req));

            // then
            assertThat(MISMATCH_CONFIRM_PASSWORD.getMessage()).isEqualTo(
                exception.getResultCode().getMessage());
        }

        @Test
        @DisplayName("실패 : 이전 비밀번호와 같음")
        void updatePasswordFailTest3() {
            // given
            UserUpdatePasswordReq req = UserUpdatePasswordReq.builder()
                .prePassword(TEST_USER_PASSWORD)
                .newPassword(TEST_USER_PASSWORD)
                .checkNewPassword(TEST_USER_PASSWORD)
                .build();
            ReflectionTestUtils.setField(testUser, "id", TEST_USER_ID);

            given(userRepository.findById(TEST_USER_ID)).willReturn(Optional.of(testUser));
            given(passwordEncoder.matches(eq(TEST_USER_PASSWORD), any())).willReturn(true);

            // when
            GlobalException exception = assertThrows(GlobalException.class,
                () -> userService.updatePassword(TEST_USER_ID, req));

            // then
            assertThat(SAME_OLD_PASSWORD.getMessage()).isEqualTo(
                exception.getResultCode().getMessage());
        }
    }

    @Nested
    @DisplayName("프로필 조회")
    class getUserProfile {

        @Test
        @DisplayName("성공")
        void getUserProfileSuccess() {
            given(userRepository.findById(anyLong())).willReturn(Optional.of(testUser));

            userService.getProfile(TEST_USER_ID);

            verify(userRepository, times(1)).findById(anyLong());
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 유저")
        void getUserProfileFail_NotFoundUser() {
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());

            GlobalException exception = assertThrows(GlobalException.class, () ->
                userService.getProfile(TEST_USER_ID));

            verify(userRepository, times(1)).findById(anyLong());

            assertThat(exception.getResultCode().getMessage()).isEqualTo(
                NOT_FOUND_USER.getMessage());
        }
    }

    @Nested
    @DisplayName("프로필 수정")
    class editUserProfile {

        @Test
        @DisplayName("성공 : 다른 프로필 이미지")
        void editUserProfileSuccess_AnotherProfileImage() throws IOException {
            UserProfileEditReq req = UserProfileEditReq.builder()
                .username("프로필 수정")
                .build();

            Resource fileResource = new ClassPathResource(TEST_ANOTHER_USER_PROFILE_IMAGE);
            MultipartFile multipartFile = new MockMultipartFile(
                "image", // 파라미터 이름
                fileResource.getFilename(), // 파일 이름
                IMAGE_PNG_VALUE, // 컨텐츠 타입
                fileResource.getInputStream()); // 컨텐츠 내용

            given(userRepository.findById(anyLong())).willReturn(Optional.of(testUser));
            given(s3Util.uploadFile(any(), any())).willReturn(TEST_ANOTHER_USER_PROFILE_IMAGE);

            userService.editProfile(testUser.getId(), req, multipartFile, testUser.getId());

            verify(userRepository, times(1)).findById(anyLong());
            verify(s3Util, times(1)).uploadFile(any(), any());
            verify(s3Util, times(1)).deleteFile(any(), any());

            assertThat(testUser.getUsername()).isEqualTo(req.getUsername());
            assertThat(testUser.getProfileImage()).isEqualTo(TEST_ANOTHER_USER_PROFILE_IMAGE);
        }

        @Test
        @DisplayName("성공 : 같은 프로필 이미지")
        void editUserProfileSuccess_SameProfileImage() {
            UserProfileEditReq req = UserProfileEditReq.builder()
                .username("프로필 수정")
                .build();

            given(userRepository.findById(anyLong())).willReturn(Optional.of(testUser));

            userService.editProfile(testUser.getId(), req, null, testUser.getId());

            verify(userRepository, times(1)).findById(anyLong());
            verify(s3Util, times(0)).uploadFile(null, null);
            verify(s3Util, times(0)).deleteFile(null, null);

            assertThat(testUser.getUsername()).isEqualTo(req.getUsername());
            assertThat(testUser.getProfileImage()).isEqualTo(TEST_USER_PROFILE_IMAGE);
        }

        @Test
        @DisplayName("실패 : 본인 프로필이 아닐 경우")
        void editUserProfileFail_Access_Deny() throws IOException {
            UserProfileEditReq req = UserProfileEditReq.builder()
                .username("프로필 수정")
                .build();

            Resource fileResource = new ClassPathResource(TEST_ANOTHER_USER_PROFILE_IMAGE);
            MultipartFile multipartFile = new MockMultipartFile(
                "image", // 파라미터 이름
                fileResource.getFilename(), // 파일 이름
                IMAGE_PNG_VALUE, // 컨텐츠 타입
                fileResource.getInputStream()); // 컨텐츠 내용

            GlobalException exception = assertThrows(GlobalException.class,
                () -> userService.editProfile(TEST_USER_ID, req, multipartFile,
                    ANOTHER_TEST_USER_ID));

            assertThat(exception.getResultCode().getMessage()).isEqualTo(ACCESS_DENY.getMessage());
        }

        @Test
        @DisplayName("실패 : 파일이 png가 아닐 경우")
        void editUserProfileFail_NOT_PNG() throws IOException {
            UserProfileEditReq req = UserProfileEditReq.builder()
                .username("프로필 수정")
                .build();

            Resource fileResource = new ClassPathResource(TEST_ANOTHER_USER_PROFILE_IMAGE);
            MultipartFile multipartFile = new MockMultipartFile(
                "image", // 파라미터 이름
                fileResource.getFilename(), // 파일 이름
                IMAGE_GIF_VALUE, // 컨텐츠 타입
                fileResource.getInputStream()); // 컨텐츠 내용

            given(userRepository.findById(anyLong())).willReturn(Optional.of(testUser));

            GlobalException exception = assertThrows(GlobalException.class,
                () -> userService.editProfile(TEST_USER_ID, req, multipartFile,
                    TEST_USER_ID));

            assertThat(exception.getResultCode().getMessage()).isEqualTo(
                INVALID_PROFILE_IMAGE_TYPE.getMessage());
        }
    }
}