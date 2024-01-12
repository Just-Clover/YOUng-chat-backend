package com.clover.youngchat.domain.user.controller;

import static com.clover.youngchat.global.exception.ResultCode.SUCCESS;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clover.youngchat.domain.BaseMvcTest;
import com.clover.youngchat.domain.user.dto.request.UserSignupReq;
import com.clover.youngchat.domain.user.dto.request.UserUpdatePasswordReq;
import com.clover.youngchat.domain.user.dto.response.UserProfileGetRes;
import com.clover.youngchat.domain.user.dto.response.UserUpdatePasswordRes;
import com.clover.youngchat.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

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
        ;
        given(userService.getProfile(anyLong())).willReturn(UserProfileGetRes.builder()
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
}