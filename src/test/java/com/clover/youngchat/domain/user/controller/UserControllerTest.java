package com.clover.youngchat.domain.user.controller;

import static com.clover.youngchat.global.exception.ResultCode.SUCCESS;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.clover.youngchat.domain.BaseMvcTest;
import com.clover.youngchat.domain.user.dto.request.UserSignupReq;
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
        UserSignupReq userSignupReq = UserSignupReq.builder()
            .email(TEST_USER_EMAIL)
            .username(TEST_USER_NAME)
            .password(TEST_USER_PASSWORD)
            .build();

        // when - then
        mockMvc.perform(post("/api/v1/users/signup")
                .content(objectMapper.writeValueAsString(userSignupReq))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(SUCCESS.getCode())))
            .andDo(print());
    }
}