package com.clover.youngchat.domain.friend.service;

import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_USER;

import com.clover.youngchat.domain.friend.dto.response.FriendAddRes;
import com.clover.youngchat.domain.friend.entity.Friend;
import com.clover.youngchat.domain.friend.repository.FriendRepository;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import com.clover.youngchat.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    public FriendAddRes addFriend(Long friendId, User user) {
        User friendUser = userRepository.findById(friendId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_USER));

        Friend friend = Friend.builder()
            .user(user)
            .friend(friendUser)
            .build();

        friendRepository.save(friend);

        return new FriendAddRes();
    }
}
