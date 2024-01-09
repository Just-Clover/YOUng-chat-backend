package com.clover.youngchat.domain.friend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clover.youngchat.domain.friend.entity.Friend;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import test.UserTest;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FriendRepositoryTest implements UserTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Test
    @DisplayName("친구를 추가하였을 때 DB에 저장되는지 확인한다.")
    void saveTest() {
        // given
        User user = userRepository.save(TEST_USER);
        User anotherUser = userRepository.save(TEST_ANOTHER_USER);

        Friend friend = Friend.builder()
            .user(user)
            .friend(anotherUser)
            .build();

        // when
        Friend actual = friendRepository.save(friend);

        // then
        assertThat(actual.getUser()).extracting("email", "username")
            .contains(TEST_USER_EMAIL, TEST_USER_NAME);

        assertThat(actual.getFriend()).extracting("email", "username")
            .contains(TEST_ANOTHER_USER_EMAIL, TEST_ANOTHER_USER_NAME);
    }
}