package com.clover.youngchat.domain.chat.service;

import static com.clover.youngchat.global.exception.ResultCode.ACCESS_DENY;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_CHATROOM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static test.ChatUserTest.TEST_CHAT_USER;

import com.clover.youngchat.domain.chat.dto.request.ChatCreateReq;
import com.clover.youngchat.domain.chat.entity.Chat;
import com.clover.youngchat.domain.chat.repository.ChatRepository;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomRepository;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomUserRepository;
import com.clover.youngchat.global.exception.GlobalException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.ChatTest;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest implements ChatTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatRoomUserRepository chatRoomUserRepository;

    @InjectMocks
    private ChatService chatService;

    @Nested
    @DisplayName("채팅 입력 테스트")
    class createChatTest {

        ChatCreateReq req;

        @BeforeEach
        void setup() {
            req = ChatCreateReq.builder()
                .message(TEST_CHAT_MESSAGE)
                .build();
        }

        @Test
        @DisplayName("성공")
        void createChatSuccessTest() {
            // given
            given(chatRoomRepository.findById(TEST_CHAT_ROOM_ID)).willReturn(
                Optional.of(TEST_CHAT_ROOM));
            given(
                chatRoomUserRepository.findByChatRoom_IdAndUser_Id(TEST_CHAT_ROOM_ID, TEST_USER_ID))
                .willReturn(Optional.of(TEST_CHAT_USER));

            // when
            chatService.createChat(TEST_CHAT_ROOM_ID, req, TEST_USER_ID);

            // then
            verify(chatRepository).save(any(Chat.class));
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 채팅방 id")
        void createChatFailTest_notFoundChatRoom() {
            // given

            // when
            GlobalException exception = assertThrows(GlobalException.class,
                () -> chatService.createChat(TEST_CHAT_ROOM_ID, req, TEST_USER_ID));

            // then
            assertThat(exception.getResultCode().getMessage())
                .isEqualTo(NOT_FOUND_CHATROOM.getMessage());
        }

        @Test
        @DisplayName("실패 : 채팅룸에 없는 유저가 채팅 입력 시도")
        void createChatFailTest_accessDeny() {
            // given
            given(chatRoomRepository.findById(TEST_CHAT_ROOM_ID)).willReturn(
                Optional.of(TEST_CHAT_ROOM));

            // when
            GlobalException exception = assertThrows(GlobalException.class,
                () -> chatService.createChat(TEST_CHAT_ROOM_ID, req, TEST_USER_ID));

            // then
            assertThat(exception.getResultCode().getMessage())
                .isEqualTo(ACCESS_DENY.getMessage());
        }
    }
}