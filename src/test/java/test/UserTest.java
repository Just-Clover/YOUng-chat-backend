package test;

import com.clover.youngchat.domain.user.entity.User;

public interface UserTest {

    Long TEST_USER_ID = 1L;
    Long ANOTHER_TEST_USER_ID = 2L;

    String TEST_USER_EMAIL = "username@email.com";
    String TEST_USER_NAME = "username";
    String TEST_USER_PASSWORD = "12345a!_";
    String TEST_USER_PROFILE_IMAGE = "images/profileImage.png";

    String TEST_ANOTHER_USER_EMAIL = "another@email.com";
    String TEST_ANOTHER_USER_NAME = "another1";
    String TEST_ANOTHER_USER_PASSWORD = "12345a!!_";
    String TEST_ANOTHER_USER_PROFILE_IMAGE = "images/imdie.png";

    User TEST_USER = User.builder()
        .email(TEST_USER_EMAIL)
        .username(TEST_USER_NAME)
        .password(TEST_USER_PASSWORD)
        .profileImage(TEST_USER_PROFILE_IMAGE)
        .build();

    User TEST_ANOTHER_USER = User.builder()
        .email(TEST_ANOTHER_USER_EMAIL)
        .username(TEST_ANOTHER_USER_NAME)
        .password(TEST_ANOTHER_USER_PASSWORD)
        .profileImage(TEST_ANOTHER_USER_PROFILE_IMAGE)
        .build();
}
