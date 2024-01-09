package com.clover.youngchat.domain.friend.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.clover.youngchat.domain.friend.entity.Friend;
import com.clover.youngchat.domain.friend.repository.FriendRepository;
import com.clover.youngchat.domain.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.UserTest;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest implements UserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendRepository friendRepository;

    @InjectMocks
    private FriendService friendService;

    @DisplayName("친구 추가 테스트")
    @Test
    void addFriendTest() {
        // given
        Long friendId = 2L;

        given(userRepository.findById(anyLong())).willReturn(Optional.of(TEST_USER));

        // when
        friendService.addFriend(friendId, TEST_USER);

        // then
        verify(userRepository, times(1)).findById(anyLong());
        verify(friendRepository, times(1)).save(any(Friend.class));
    }
}
