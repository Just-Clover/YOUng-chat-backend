package com.clover.youngchat.domain.chatRoom.controller;

import static com.clover.youngchat.global.exception.ResultCode.SUCCESS;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static test.ChatRoomTest.TEST_CHAT_ROOM_TITLE;

import com.clover.youngchat.domain.BaseMvcTest;
import com.clover.youngchat.domain.chatroom.controller.ChatRoomController;
import com.clover.youngchat.domain.chatroom.dto.request.ChatRoomCreateReq;
import com.clover.youngchat.domain.chatroom.service.ChatRoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = {ChatRoomController.class})
public class ChatRoomControllerTest extends BaseMvcTest {

    @MockBean
    private ChatRoomService chatRoomService;

    @Test
    @DisplayName("채팅방 생성 테스트 : 성공")
    void createChatRoom() throws Exception {
        ChatRoomCreateReq req = ChatRoomCreateReq.builder()
            .title(TEST_CHAT_ROOM_TITLE)
            .friendId(ANOTHER_TEST_USER_ID)
            .build();

        mockMvc.perform(post("/api/v1/chat-rooms")
                .content(objectMapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code", is(SUCCESS.getCode())))
            .andDo(print());
    }
}
