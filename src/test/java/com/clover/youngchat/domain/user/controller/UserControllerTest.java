package com.clover.youngchat.domain.user.controller;

import static com.clover.youngchat.global.exception.ResultCode.SUCCESS;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clover.youngchat.domain.BaseMvcTest;
import com.clover.youngchat.domain.user.dto.request.UserProfileEditReq;
import com.clover.youngchat.domain.user.dto.request.UserSignupReq;
import com.clover.youngchat.domain.user.dto.request.UserUpdatePasswordReq;
import com.clover.youngchat.domain.user.dto.response.UserProfileEditRes;
import com.clover.youngchat.domain.user.dto.response.UserProfileGetRes;
import com.clover.youngchat.domain.user.dto.response.UserProfileSearchRes;
import com.clover.youngchat.domain.user.dto.response.UserUpdatePasswordRes;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.service.UserService;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@WebMvcTest(controllers = {UserController.class})
class UserControllerTest extends BaseMvcTest {

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("회원가입 테스트 : 성공")
    void signupTestSuccess() throws Exception {
        // given
        UserSignupReq req = UserSignupReq.builder()
            .email(TEST_USER_EMAIL)
            .username(TEST_USER_NAME)
            .password(TEST_USER_PASSWORD)
            .build();

        // when - then
        mockMvc.perform(post("/api/v1/users/signup")
                .content(objectMapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(SUCCESS.getCode())))
            .andDo(print());
    }

    @Test
    @DisplayName("이메일로 사용자 검색 테스트")
    void findUserByEmailTest() throws Exception {
        // given
        given(userService.searchProfile(anyString())).willReturn(
            UserProfileSearchRes.builder()
                .userId(TEST_USER_ID)
                .email(TEST_USER_EMAIL)
                .profileImage(TEST_USER_PROFILE_IMAGE)
                .build()
        );
        
        // when - then
        mockMvc.perform(get("/api/v1/users/search")
                .header("Keyword", TEST_USER_EMAIL)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(SUCCESS.getCode())))
            .andDo(print());
    }

    @Test
    @DisplayName("비밀번호변경 테스트 : 성공")
    void updatePasswordTestSuccess() throws Exception {
        // given
        UserUpdatePasswordReq req = UserUpdatePasswordReq.builder()
            .prePassword(TEST_USER_PASSWORD)
            .newPassword(TEST_ANOTHER_USER_PASSWORD)
            .checkNewPassword(TEST_ANOTHER_USER_PASSWORD)
            .build();

        given(userService.updatePassword(any(), any())).willReturn(new UserUpdatePasswordRes());

        // when - then
        mockMvc.perform(patch("/api/v1/users/password")
                .content(objectMapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal))
            .andExpect(jsonPath("$.code", is(SUCCESS.getCode())))
            .andDo(print());
    }

    @Test
    @DisplayName("프로필 조회 : 성공")
    void getUserProfileSuccess() throws Exception {
        Long userId = 1L;

        given(userService.getProfile(anyLong(), any(User.class))).willReturn(
            UserProfileGetRes.builder()
                .username(TEST_USER_NAME)
                .profileImage(TEST_USER_PROFILE_IMAGE)
                .build());

        mockMvc.perform(get("/api/v1/users/profile")
                .param("userId", String.valueOf(userId))
                .principal(mockPrincipal))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code", is(SUCCESS.getCode())))
            .andDo(print());
    }

    @Test
    @DisplayName("프로필 수정 : 성공")
    void editUserProfileSuccess() throws Exception {
        Long userId = 1L;

        given(userService.editProfile(anyLong(), any(), any(), anyLong())).willReturn(
            new UserProfileEditRes());
        UserProfileEditReq req = UserProfileEditReq.builder()
            .username(TEST_USER_NAME)
            .build();
        Resource fileResource = new ClassPathResource(TEST_USER_PROFILE_IMAGE);

        MockMultipartFile multipartFile = new MockMultipartFile(
            "image", // 파라미터 이름
            fileResource.getFilename(), // 파일 이름
            IMAGE_PNG_VALUE, // 컨텐츠 타입
            fileResource.getInputStream()); // 컨텐츠 내용

        String reqToJson = objectMapper.writeValueAsString(req);

        MockMultipartFile reqFile = new MockMultipartFile(
            "req", "json", "application/json", reqToJson.getBytes(StandardCharsets.UTF_8));
        mockMvc
            .perform(
                multipart(HttpMethod.PATCH, "/api/v1/users/profile").file(multipartFile)
                    .file(reqFile).principal(mockPrincipal)
                    .param("userId", String.valueOf(userId)))
            .andDo(print())
            .andExpect(status().isOk());
    }
}