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
}
