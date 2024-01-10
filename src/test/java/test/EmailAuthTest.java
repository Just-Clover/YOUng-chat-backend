package test;

import com.clover.youngchat.domain.user.entity.EmailAuth;

public interface EmailAuthTest extends UserTest {

    String TEST_EMAIL = "test@gmail.com";

    String TEST_CODE = "e908942b";

    EmailAuth TEST_EMAIL_AUTH =
        EmailAuth.builder()
            .email(TEST_EMAIL)
            .code(TEST_CODE)
            .isAuthenticated(false)
            .build();

    EmailAuth TEST_EMAIL_AUTH_OK =
        EmailAuth.builder()
            .email(TEST_EMAIL)
            .code(TEST_CODE)
            .isAuthenticated(true)
            .build();

    EmailAuth TEST_EMAIL_AUTH_FAIL =
        EmailAuth.builder()
            .email(TEST_EMAIL)
            .code(TEST_CODE)
            .isAuthenticated(false)
            .build();
}
