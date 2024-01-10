package test;

import com.clover.youngchat.domain.chatroom.entity.ChatUser;

public interface ChatUserTest extends UserTest, ChatRoomTest {

    Long TEST_CHAT_USER_ID = 1L;

    ChatUser TEST_CHAT_USER = ChatUser.builder()
        .user(TEST_USER)
        .chatRoom(TEST_CHAT_ROOM)
        .build();
}
