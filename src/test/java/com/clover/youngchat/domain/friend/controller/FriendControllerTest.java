package com.clover.youngchat.domain.friend.controller;

import static com.clover.youngchat.global.exception.ResultCode.SUCCESS;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.clover.youngchat.domain.BaseMvcTest;
import com.clover.youngchat.domain.friend.service.command.FriendCommandService;
import com.clover.youngchat.domain.friend.service.query.FriendQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = FriendController.class)
public class FriendControllerTest extends BaseMvcTest {

    @MockBean
    private FriendQueryService friendQueryService;

    @MockBean
    private FriendCommandService friendCommandService;

    @Test
    @DisplayName("친구목록 조회 테스트")
    void getFriendListTest() throws Exception {
        // when - then
        mockMvc.perform(get("/api/v1/friends")
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(SUCCESS.getCode())))
            .andDo(print());
    }

    @Test
    @DisplayName("친구목록 검색 조회 테스트")
    void getFriendSearchListTest() throws Exception {
        // given
        String keyword = "test";

        // when - then
        mockMvc.perform(get("/api/v1/friends/search")
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON).param("keyword", keyword))
            .andExpect(jsonPath("$.code", is(SUCCESS.getCode())))
            .andDo(print());
    }

    @Test
    @DisplayName("친구추가 테스트")
    void addFriendTest() throws Exception {
        // given
        long friendId = 1L;

        // when - then
        mockMvc.perform(post("/api/v1/friends/" + friendId)
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(SUCCESS.getCode())))
            .andDo(print());
    }

    @Test
    @DisplayName("친구삭제 테스트")
    void deleteFriendTest() throws Exception {
        // given
        long friendId = 1L;

        // when - then
        mockMvc.perform(delete("/api/v1/friends/" + friendId)
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(SUCCESS.getCode())))
            .andDo(print());
    }

}
