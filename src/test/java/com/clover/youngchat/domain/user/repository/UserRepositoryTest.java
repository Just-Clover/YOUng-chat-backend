package com.clover.youngchat.domain.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clover.youngchat.domain.user.entity.User;
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
class UserRepositoryTest implements UserTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        // when
        User saveUser = userRepository.save(TEST_USER);

        // then
        assertThat(saveUser.getUsername()).isEqualTo(TEST_USER_NAME);
        assertThat(saveUser.getEmail()).isEqualTo(TEST_USER_EMAIL);
    }

    @Test
    @DisplayName("email 중복 확인")
    void existsByEmailTest() {
        // given
        User saveUser = userRepository.save(TEST_USER);

        // when
        boolean isDuplicated = userRepository.existsByEmail(saveUser.getEmail());

        // then
        assertThat(isDuplicated).isTrue();
    }
}
