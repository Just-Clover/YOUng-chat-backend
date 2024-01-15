package test;

import com.clover.youngchat.domain.chatroom.entity.ChatRoomUser;
import java.util.Arrays;
import java.util.List;

public interface ChatRoomUserTest extends UserTest, ChatRoomTest {

    Long TEST_CHAT_ROOM_USER_ID = 1L;
    Long TEST_ANOTHER_CHAT_ROOM_USER_ID = 2L;

    ChatRoomUser TEST_CHAT_ROOM_USER = ChatRoomUser.builder()
        .user(TEST_USER)
        .chatRoom(TEST_CHAT_ROOM)
        .build();

    ChatRoomUser TEST_ANOTHER_CHAT_ROOM_USER = ChatRoomUser.builder()
        .user(TEST_ANOTHER_USER)
        .chatRoom(TEST_ANOTHER_CHAT_ROOM)
        .build();

    List<ChatRoomUser> TEST_CHAT_ROOM_USER_LIST = Arrays.asList(TEST_CHAT_ROOM_USER,
        TEST_ANOTHER_CHAT_ROOM_USER);

}