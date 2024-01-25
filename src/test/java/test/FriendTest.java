package test;

import com.clover.youngchat.domain.friend.entity.Friend;
import com.clover.youngchat.domain.user.entity.User;

public interface FriendTest extends UserTest {

    String TEST_FRIEND_USER_EMAIL = "test@email.com";
    String TEST_FRIEND_USER_NAME = "username";
    String TEST_FRIEND_USER_PASSWORD = "12345678";

    User TEST_FRIEND_USER = User.builder()
        .email(TEST_FRIEND_USER_EMAIL)
        .username(TEST_FRIEND_USER_NAME)
        .password(TEST_FRIEND_USER_PASSWORD)
        .build();

    Friend TEST_FRIEND = Friend.builder()
        .user(TEST_USER)
        .friend(TEST_ANOTHER_USER)
        .build();

    Friend TEST_ANOTHER_FRIEND = Friend.builder()
        .user(TEST_USER)
        .friend(TEST_FRIEND_USER)
        .build();
}
