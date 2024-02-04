package com.clover.youngchat.domain.friend.service.query;

import com.clover.youngchat.domain.friend.dto.response.FriendGetListRes;
import com.clover.youngchat.domain.friend.dto.response.FriendGetSearchListRes;
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
@Transactional(readOnly = true)
public class FriendQueryService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    public List<FriendGetListRes> getFriendList(User user) {
        List<Friend> friends = findByUser(user);

        return friends.stream()
            .map(friend -> FriendGetListRes.to(friend.getFriend().getId(),
                friend.getFriend().getUsername(),
                friend.getFriend().getProfileImage()))
            .collect(Collectors.toList());
    }

    public List<FriendGetSearchListRes> getFriendSearchList(String keyword, User user) {
        return friendRepository.findFriendByKeyword(user.getId(), keyword);
    }

    private List<Friend> findByUser(User user) {
        return friendRepository.findByUser(user)
            .orElseThrow(() -> new GlobalException(ResultCode.NOT_FOUND_FRIEND));
    }
}
