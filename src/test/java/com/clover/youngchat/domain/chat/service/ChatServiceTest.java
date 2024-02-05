package com.clover.youngchat.domain.chat.service;

import static com.clover.youngchat.global.exception.ResultCode.ACCESS_DENY;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_CHAT;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_CHATROOM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.clover.youngchat.domain.chat.dto.request.ChatCreateReq;
import com.clover.youngchat.domain.chat.dto.request.ChatDeleteReq;
import com.clover.youngchat.domain.chat.dto.response.ChatRes;
import com.clover.youngchat.domain.chat.repository.ChatRepository;
import com.clover.youngchat.domain.chat.service.command.ChatCommandService;
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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import test.ChatTest;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest implements ChatTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatRoomUserRepository chatRoomUserRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ChatCommandService chatCommandService;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Nested
    @DisplayName("채팅 입력 테스트")
    class createChatTest {

        ChatCreateReq req;

        @BeforeEach
        void setup() {
            req = ChatCreateReq.builder()
                .message(TEST_CHAT_MESSAGE)
                .userId(TEST_USER_ID)
                .build();
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 채팅방 id")
        void createChatFailTest_notFoundChatRoom() {
            // given
            given(chatRoomRepository.findById(TEST_CHAT_ROOM_ID)).willReturn(
                Optional.empty());

            // when
            GlobalException exception = assertThrows(GlobalException.class,
                () -> chatCommandService.sendMessage(TEST_CHAT_ROOM_ID, req));

            // then
            assertThat(exception.getResultCode().getMessage())
                .isEqualTo(NOT_FOUND_CHATROOM.getMessage());
        }
    }

    @Nested
    @DisplayName("채팅 삭제 테스트")
    class deleteChatTest {

        ChatDeleteReq req;

        @BeforeEach
        void setup() {
            req = ChatDeleteReq.builder()
                .userId(TEST_USER_ID)
                .chatId(TEST_CHAT_ID)
                .build();
        }

        @Test
        @DisplayName("성공")
        void deleteChatSuccessTest() {
            // given
            given(chatRoomUserRepository.existsByChatRoom_IdAndUser_Id(any(), any()))
                .willReturn(true);
            given(chatRepository.findById(any())).willReturn(Optional.of(TEST_CHAT));

            // when
            chatCommandService.deleteChat(TEST_CHAT_ROOM_ID, req);

            // then
            assertThat(TEST_CHAT.isDeleted()).isTrue();
            verify(rabbitTemplate).convertAndSend(eq(exchangeName),
                eq("chat-rooms." + TEST_CHAT_ROOM_ID),
                any(ChatRes.class));

        }

        @Test
        @DisplayName("실패 : 방id와 유저id와 일치한 chatuser가 없을 때")
        void deleteChatFailTest_AccessDeny() {
            // given
            given(chatRoomUserRepository.existsByChatRoom_IdAndUser_Id(any(), any()))
                .willReturn(false);

            // when
            GlobalException exception = assertThrows(GlobalException.class,
                () -> chatCommandService.deleteChat(TEST_CHAT_ROOM_ID, req));

            // then
            assertThat(exception.getResultCode().getMessage())
                .isEqualTo(ACCESS_DENY.getMessage());

        }

        @Test
        @DisplayName("실패 : 존재하지 않는 chat id로 삭제를 시도할 때")
        void deleteChatFailTest_NotFoundChat() {
            // given
            given(chatRoomUserRepository.existsByChatRoom_IdAndUser_Id(any(), any()))
                .willReturn(true);
            given(chatRepository.findById(any())).willReturn(Optional.empty());

            // when
            GlobalException exception = assertThrows(GlobalException.class,
                () -> chatCommandService.deleteChat(TEST_CHAT_ROOM_ID, req));

            // then
            assertThat(exception.getResultCode().getMessage())
                .isEqualTo(NOT_FOUND_CHAT.getMessage());
        }
    }
}
