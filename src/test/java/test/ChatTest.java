package test;

import com.clover.youngchat.domain.chat.entity.Chat;

public interface ChatTest extends UserTest, ChatRoomTest {

    String TEST_CHAT_MESSAGE = "test message";
    String TEST_ANOTHER_CHAT_MESSAGE = "test message2";

    Chat TEST_CHAT = Chat.builder()
        .message(TEST_CHAT_MESSAGE)
        .sender(TEST_USER)
        .chatRoom(TEST_CHAT_ROOM)
        .build();

    Chat TEST_ANOTHER_CHAT = Chat.builder()
        .message(TEST_ANOTHER_CHAT_MESSAGE)
        .sender(TEST_ANOTHER_USER)
        .chatRoom(TEST_ANOTHER_CHAT_ROOM)
        .build();
}
