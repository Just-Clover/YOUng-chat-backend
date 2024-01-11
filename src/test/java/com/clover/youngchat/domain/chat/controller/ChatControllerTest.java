package com.clover.youngchat.domain.chat.controller;

import static com.clover.youngchat.global.exception.ResultCode.SUCCESS;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.clover.youngchat.domain.BaseMvcTest;
import com.clover.youngchat.domain.chat.dto.request.ChatCreateReq;
import com.clover.youngchat.domain.chat.service.ChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = ChatController.class)
class ChatControllerTest extends BaseMvcTest {

    @MockBean
    private ChatService chatService;

    @Test
    @DisplayName("채팅입력 테스트 : 성공")
    void createChatTestSuccess() throws Exception {
        // given
        long chatRoomId = 1L;
        ChatCreateReq req = ChatCreateReq.builder()
            .message("test")
            .build();

        // when - then
        mockMvc.perform(post("/api/v1/chat-rooms/" + chatRoomId + "/chats")
                .principal(mockPrincipal)
                .content(objectMapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(SUCCESS.getCode())))
            .andDo(print());
    }

    @Test
    @DisplayName("채팅삭제 테스트 : 성공")
    void deleteChatTestSuccess() throws Exception {
        // given
        long chatRoomId = 1L;
        long chatId = 1L;

        // when - then
        mockMvc.perform(delete("/api/v1/chat-rooms/" + chatRoomId + "/chats/" + chatId)
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(SUCCESS.getCode())))
            .andDo(print());
    }
}