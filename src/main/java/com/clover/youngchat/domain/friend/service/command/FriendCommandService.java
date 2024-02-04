package com.clover.youngchat.domain.friend.service.command;


import static com.clover.youngchat.global.exception.ResultCode.ALREADY_FRIEND;
import static com.clover.youngchat.global.exception.ResultCode.INVALID_INPUT;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_USER;

import com.clover.youngchat.domain.friend.dto.response.FriendAddRes;
import com.clover.youngchat.domain.friend.dto.response.FriendDeleteRes;
import com.clover.youngchat.domain.friend.entity.Friend;
import com.clover.youngchat.domain.friend.repository.FriendRepository;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import com.clover.youngchat.global.exception.GlobalException;
import com.clover.youngchat.global.exception.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendCommandService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;


    public FriendAddRes addFriend(Long friendId, User user) {
        if (user.getId().equals(friendId)) {
            throw new GlobalException(INVALID_INPUT);
        }
        if (friendRepository.existsByUser_IdAndFriend_Id(user.getId(), friendId)) {
            throw new GlobalException(ALREADY_FRIEND);
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

    public FriendDeleteRes deleteFriend(Long friendId, User user) {
        if (!friendRepository.existsByUser_IdAndFriend_Id(user.getId(), friendId)) {
            throw new GlobalException(ResultCode.NOT_FOUND_FRIEND);
        }
        friendRepository.deleteByFriend_Id(friendId);

        return new FriendDeleteRes();
    }

}
