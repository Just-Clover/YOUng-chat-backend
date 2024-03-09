package test;

import com.clover.youngchat.domain.chat.entity.Chat;
import java.util.Arrays;
import java.util.List;

public interface ChatTest extends UserTest, ChatRoomTest {

    String TEST_CHAT_ID = "1";
    Long TEST_ANOTHER_CHAT_ID = 2L;

    String TEST_CHAT_MESSAGE = "test message";
    String TEST_ANOTHER_CHAT_MESSAGE = "test message2";

    Chat TEST_CHAT = Chat.builder()
        .message(TEST_CHAT_MESSAGE)
        .senderId(TEST_USER.getId())
        .chatRoomId(TEST_CHAT_ROOM.getId())
        .build();

    Chat TEST_ANOTHER_CHAT = Chat.builder()
        .message(TEST_ANOTHER_CHAT_MESSAGE)
        .senderId(TEST_ANOTHER_USER.getId())
        .chatRoomId(TEST_ANOTHER_CHAT_ROOM.getId())
        .build();

    List<Chat> TEST_CHAT_LIST = Arrays.asList(TEST_CHAT, TEST_ANOTHER_CHAT);
}
