//package com.clover.youngchat.domain.friend.repository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.clover.youngchat.domain.friend.dto.response.FriendGetSearchListRes;
//import com.clover.youngchat.domain.friend.entity.Friend;
//import com.clover.youngchat.domain.user.entity.User;
//import com.clover.youngchat.domain.user.repository.UserRepository;
//import com.clover.youngchat.global.config.QueryDslConfig;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//import test.FriendTest;
//
//
//@DataJpaTest
//@ActiveProfiles("test")
//@Import(QueryDslConfig.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class FriendRepositoryTest implements FriendTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private FriendRepository friendRepository;
//
//    @Test
//    @DisplayName("본인의 친구 목록을 조회한다.")
//    void getFriendList_Test() {
//        // given
//        User user = userRepository.save(TEST_USER);
//        userRepository.save(TEST_ANOTHER_USER);
//        userRepository.save(TEST_FRIEND_USER);
//        friendRepository.save(TEST_FRIEND);
//        friendRepository.save(TEST_ANOTHER_FRIEND);
//
//        // when
//        List<Friend> actual = friendRepository.findByUser(user).get();
//
//        // then
//        assertThat(actual).hasSize(2);
//        assertThat(actual.get(0).getFriend().getUsername()).isEqualTo(TEST_ANOTHER_USER_NAME);
//    }
//
//    @Test
//    @DisplayName("키워드로 친구 목록을 검색한다.")
//    void getSearch_Test() {
//        // given
//        User user = userRepository.save(TEST_USER);
//        User anotherUser = userRepository.save(TEST_ANOTHER_USER);
//        User friendUser = userRepository.save(TEST_FRIEND_USER);
//
//        Friend friend = Friend.builder()
//            .user(user)
//            .friend(anotherUser)
//            .build();
//
//        Friend friend2 = Friend.builder()
//            .user(user)
//            .friend(friendUser)
//            .build();
//
//        friendRepository.save(friend);
//        friendRepository.save(friend2);
//
//        String keyword = "a";
//
//        // when
//        List<FriendGetSearchListRes> actual = friendRepository.findFriendByKeyword(
//            user.getId(), keyword);
//
//        // then
//        assertThat(actual).hasSize(2);
//    }
//
//    @Test
//    @DisplayName("친구를 추가하였을 때 DB에 저장되는지 확인한다.")
//    void save_Test() {
//        // given
//        User user = userRepository.save(TEST_USER);
//        User anotherUser = userRepository.save(TEST_ANOTHER_USER);
//
//        Friend friend = Friend.builder()
//            .user(user)
//            .friend(anotherUser)
//            .build();
//
//        // when
//        Friend actual = friendRepository.save(friend);
//
//        // then
//        assertThat(actual.getUser()).extracting("email", "username")
//            .contains(TEST_USER_EMAIL, TEST_USER_NAME);
//
//        assertThat(actual.getFriend()).extracting("email", "username")
//            .contains(TEST_ANOTHER_USER_EMAIL, TEST_ANOTHER_USER_NAME);
//    }
//
//    @Test
//    @DisplayName("유저의 Id와 친구의 Id가 존재하는지 체크한다.")
//    void existsByUser_IdAndFriend_Id_Test() {
//        // given
//        User user = userRepository.save(TEST_USER);
//        User anotherUser = userRepository.save(TEST_ANOTHER_USER);
//
//        Friend friend = Friend.builder()
//            .user(user)
//            .friend(anotherUser)
//            .build();
//
//        friendRepository.save(friend);
//        // when
//        boolean actual = friendRepository.existsByUser_IdAndFriend_Id(user.getId(),
//            anotherUser.getId());
//
//        // then
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    @DisplayName("친구가 삭제되었는지 테스트한다.")
//    void deleteByFriend_Id_Test() {
//        // given
//        User user = userRepository.save(TEST_USER);
//        User anotherUser = userRepository.save(TEST_ANOTHER_USER);
//
//        Friend friend = Friend.builder()
//            .user(user)
//            .friend(anotherUser)
//            .build();
//
//        friendRepository.save(friend);
//        // when
//
//        friendRepository.deleteByFriend_Id(anotherUser.getId());
//
//        boolean actual = friendRepository.existsByUser_IdAndFriend_Id(user.getId(),
//            anotherUser.getId());
//
//        // then
//        assertThat(actual).isFalse();
//    }
//}