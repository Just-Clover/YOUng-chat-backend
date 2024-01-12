package test;

import com.clover.youngchat.domain.user.entity.User;

public interface UserTest {

    Long TEST_USER_ID = 1L;
    Long ANOTHER_TEST_USER_ID = 2L;

    String TEST_USER_EMAIL = "username@email.com";
    String TEST_USER_NAME = "username";
    String TEST_USER_PASSWORD = "12345aA!";
    String TEST_USER_PROFILE_IMAGE = "https://bucket.s3.ap-northeast-2.amazonaws.com/profile/77607796.png";

    String TEST_ANOTHER_USER_EMAIL = "another@email.com";
    String TEST_ANOTHER_USER_NAME = "another1";
    String TEST_ANOTHER_USER_PASSWORD = "12345aA!!";
    String TEST_ANOTHER_USER_PROFILE_IMAGE = "https://bucket.s3.ap-northeast-2.amazonaws.com/profile/123456789.png";

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
