package com.clover.youngchat.domain.friend.repository;

import com.clover.youngchat.domain.friend.dto.response.FriendGetSearchListRes;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepositoryCustom {

    List<FriendGetSearchListRes> findFriendByKeyword(Long userId, String keyword);
}
