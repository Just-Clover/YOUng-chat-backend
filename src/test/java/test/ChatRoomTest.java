package test;

import com.clover.youngchat.domain.chatroom.entity.ChatRoom;

public interface ChatRoomTest extends UserTest {

    Long TEST_CHAT_ROOM_ID = 1L;
    Long TEST_ANOTHER_CHAT_ROOM_ID = 2L;

    String TEST_CHAT_ROOM_TITLE = "test title";
    String TEST_ANOTHER_CHAT_ROOM_TITLE = "test title";

    ChatRoom TEST_CHAT_ROOM = ChatRoom.builder()
        .title(TEST_CHAT_ROOM_TITLE)
        .build();

    ChatRoom TEST_ANOTHER_CHAT_ROOM = ChatRoom.builder()
        .title(TEST_ANOTHER_CHAT_ROOM_TITLE)
        .build();
}
