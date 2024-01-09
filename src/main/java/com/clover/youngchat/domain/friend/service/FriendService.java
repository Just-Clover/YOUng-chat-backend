package com.clover.youngchat.domain.friend.service;

import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_USER;

import com.clover.youngchat.domain.friend.dto.response.FriendAddRes;
import com.clover.youngchat.domain.friend.dto.response.FriendGetListRes;
import com.clover.youngchat.domain.friend.entity.Friend;
import com.clover.youngchat.domain.friend.repository.FriendRepository;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import com.clover.youngchat.global.exception.GlobalException;
import com.clover.youngchat.global.exception.ResultCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @Transactional(readOnly = true)
    public FriendGetListRes getFriendList(User user) {
        List<Friend> friends = friendRepository.findByUser(user)
            .orElseThrow(() -> new GlobalException(ResultCode.NOT_FOUND_FRIEND));

        List<String> usernameList = friends.stream()
            .map(friend -> friend.getFriend().getUsername())
            .collect(Collectors.toList());

        return FriendGetListRes.to(usernameList);
    }

    public FriendAddRes addFriend(Long friendId, User user) {
        if (user.getId().equals(friendId)) {
            throw new GlobalException(ResultCode.INVALID_INPUT);
        }
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
