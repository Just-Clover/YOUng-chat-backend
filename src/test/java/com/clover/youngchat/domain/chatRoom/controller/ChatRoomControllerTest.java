package com.clover.youngchat.domain.chatRoom.controller;

import static com.clover.youngchat.global.exception.ResultCode.SUCCESS;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static test.ChatRoomTest.TEST_CHAT_ROOM_ID;
import static test.ChatRoomTest.TEST_CHAT_ROOM_TITLE;

import com.clover.youngchat.domain.BaseMvcTest;
import com.clover.youngchat.domain.chatroom.controller.ChatRoomController;
import com.clover.youngchat.domain.chatroom.dto.request.ChatRoomEditReq;
import com.clover.youngchat.domain.chatroom.dto.request.PersonalChatRoomCreateReq;
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
    @DisplayName("1:1 채팅방 생성 테스트 : 성공")
    void createChatRoom() throws Exception {
        PersonalChatRoomCreateReq req = PersonalChatRoomCreateReq.builder()
            .title(TEST_CHAT_ROOM_TITLE)
            .friendId(ANOTHER_TEST_USER_ID)
            .build();

        mockMvc.perform(post("/api/v1/chat-rooms/private")
                .content(objectMapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code", is(SUCCESS.getCode())))
            .andDo(print());
    }

    @Test
    @DisplayName("채팅방 수정 테스트 : 성공")
    void editChatRoom() throws Exception {
        ChatRoomEditReq req = ChatRoomEditReq.builder()
            .title(TEST_CHAT_ROOM_TITLE)
            .build();

        mockMvc.perform(patch("/api/v1/chat-rooms/" + TEST_CHAT_ROOM_ID)
                .content(objectMapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code", is(SUCCESS.getCode())))
            .andDo(print());
    }

    @Test
    @DisplayName("채팅방 나가기 테스트 : 성공")
    void leaveChatRoom() throws Exception {
        mockMvc.perform(delete("/api/v1/chat-rooms/" + TEST_CHAT_ROOM_ID)
                .principal(mockPrincipal))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code", is(SUCCESS.getCode())))
            .andDo(print());
    }

    @Test
    @DisplayName("채팅방 목록 조회 테스트 : 성공")
    void getChatRoomList() throws Exception {
        mockMvc.perform(get("/api/v1/chat-rooms")
                .principal(mockPrincipal))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code", is(SUCCESS.getCode())))
            .andDo(print());
    }

    @Test
    @DisplayName("채팅방 상세 조회 테스트 : 성공")
    void getDetailChatRoom() throws Exception {
        mockMvc.perform(get("/api/v1/chat-rooms/" + TEST_CHAT_ROOM_ID)
                .principal(mockPrincipal))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code", is(SUCCESS.getCode())))
            .andDo(print());
    }
}
