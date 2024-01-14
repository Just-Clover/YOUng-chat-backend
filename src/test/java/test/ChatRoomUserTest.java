package test;

import com.clover.youngchat.domain.chatroom.entity.ChatRoomUser;

public interface ChatRoomUserTest extends UserTest, ChatRoomTest {

    Long TEST_CHAT_USER_ID = 1L;

    ChatRoomUser TEST_CHAT_ROOM_USER = ChatRoomUser.builder()
        .user(TEST_USER)
        .chatRoom(TEST_CHAT_ROOM)
        .build();
}