package com.clover.youngchat.domain.friend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clover.youngchat.domain.BaseMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(controllers = {FriendController.class})
public class FriendControllerTest extends BaseMvcTest {

    @Test
    @DisplayName("친구추가 테스트")
    void addFriendTest() throws Exception {
        // given
        long friendId = 1L;

        // when - then
        mockMvc.perform(post("/api/v1/friends/" + friendId)
                .principal(mockPrincipal))
            .andExpect(status().isOk())
            .andDo(print());
    }
}
