package test;

import com.clover.youngchat.domain.chat.entity.Chat;
import java.util.Arrays;
import java.util.List;

public interface ChatTest extends UserTest, ChatRoomTest {

    Long TEST_CHAT_ID = 1L;
    Long TEST_ANOTHER_CHAT_ID = 2L;

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

    List<Chat> TEST_CHAT_LIST = Arrays.asList(TEST_CHAT, TEST_ANOTHER_CHAT);
}
