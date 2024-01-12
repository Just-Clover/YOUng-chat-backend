package com.clover.youngchat.domain.chatRoom.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static test.ChatRoomTest.TEST_CHAT_ROOM_TITLE;
import static test.UserTest.ANOTHER_TEST_USER_ID;
import static test.UserTest.TEST_USER;

import com.clover.youngchat.domain.chatroom.dto.request.ChatRoomCreateReq;
import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomRepository;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomUserRepository;
import com.clover.youngchat.domain.chatroom.service.ChatRoomService;
import com.clover.youngchat.domain.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceTest {

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatRoomUserRepository chatRoomUserRepository;

    private ChatRoom chatRoom;

    @BeforeEach
    void setup() {
        chatRoom = ChatRoom.builder()
            .title(TEST_CHAT_ROOM_TITLE)
            .build();
    }

    @Test
    @DisplayName("채팅방 생성 성공")
    void createChatRoomSuccess() {
        ChatRoomCreateReq req = ChatRoomCreateReq.builder()
            .title(TEST_CHAT_ROOM_TITLE)
            .friendId(ANOTHER_TEST_USER_ID)
            .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.of(TEST_USER));

        chatRoomService.createChatRoom(req, TEST_USER);

        verify(userRepository, times(1)).findById(anyLong());
        verify(chatRoomRepository, times(1)).save(any());
        verify(chatRoomUserRepository, times(2)).save(any());
    }

}
