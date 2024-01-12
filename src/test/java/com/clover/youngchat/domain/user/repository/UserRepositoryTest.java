package com.clover.youngchat.domain.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.global.config.QueryDslConfig;
import java.util.List;
import java.util.Optional;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import test.UserTest;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDslConfig.class)
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
        assertThat(saveUser.getProfileImage()).isEqualTo(TEST_USER_PROFILE_IMAGE);
        assertThat(saveUser.getEmail()).isEqualTo(TEST_USER_EMAIL);
    }

    @Test
    @DisplayName("Id 리스트로 유저를 조회한다.")
    void findUsersByIdContains() {
        // given
        User user = userRepository.save(TEST_USER);
        User anotherUser = userRepository.save(TEST_ANOTHER_USER);

        // when
        List<User> actual = userRepository.findUsersByIdIn(
            List.of(user.getId(), anotherUser.getId())).get();

        // then
        assertThat(actual).hasSize(2);
        assertThat(actual).extracting("username", "email")
            .contains(Tuple.tuple(TEST_USER_NAME, TEST_USER_EMAIL),
                tuple(TEST_ANOTHER_USER_NAME, TEST_ANOTHER_USER_EMAIL));
    }

    @DisplayName("email 중복 확인")
    void existsByEmailTest() {
        // given
        User saveUser = userRepository.save(TEST_USER);

        // when
        boolean isDuplicated = userRepository.existsByEmail(saveUser.getEmail());

        // then
        assertThat(isDuplicated).isTrue();
    }

    @Test
    @DisplayName("findById 테스트")
    void findByIdTest() {
        userRepository.save(TEST_USER);

        Optional<User> findUser = userRepository.findById(TEST_USER.getId());

        assertThat(findUser.get().getId()).isEqualTo(TEST_USER.getId());
        assertThat(findUser.get().getUsername()).isEqualTo(TEST_USER_NAME);
        assertThat(findUser.get().getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(findUser.get().getProfileImage()).isEqualTo(TEST_USER_PROFILE_IMAGE);
        assertThat(findUser.get().getPassword()).matches(TEST_USER_PASSWORD);
    }
}
