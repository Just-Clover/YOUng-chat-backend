package test;

import com.clover.youngchat.domain.user.entity.User;

public interface UserTest {

    String TEST_USER_EMAIL = "username@email.com";
    String TEST_USER_NAME = "username";
    String TEST_USER_PASSWORD = "12345aA!";

    String TEST_ANOTHER_USER_EMAIL = "another@email.com";
    String TEST_ANOTHER_USER_NAME = "another1";
    String TEST_ANOTHER_USER_PASSWORD = "12345aA!!";

    User TEST_USER = User.builder()
        .email(TEST_USER_EMAIL)
        .username(TEST_USER_NAME)
        .password(TEST_USER_PASSWORD)
        .build();

    User TEST_ANOTHER_USER = User.builder()
        .email(TEST_ANOTHER_USER_EMAIL)
        .username(TEST_ANOTHER_USER_NAME)
        .password(TEST_ANOTHER_USER_PASSWORD)
        .build();
}
