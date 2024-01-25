package test;

import com.clover.youngchat.domain.auth.entity.Blacklist;

public interface BlacklistTest {

    String TEST_ACCESS_TOKEN = "testaccesstoken";
    long TEST_EXPIRATION = 1L;
    Blacklist TEST_BLACKLIST = Blacklist.builder()
        .accessToken(TEST_ACCESS_TOKEN)
        .expiration(TEST_EXPIRATION)
        .build();
}
