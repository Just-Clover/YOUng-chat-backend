package com.clover.youngchat.domain.friend.repository;

import com.clover.youngchat.domain.friend.entity.Friend;
import com.clover.youngchat.domain.friend.entity.FriendPK;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Friend.class, idClass = FriendPK.class)
public interface FriendRepository {
    Friend save(Friend friend);
}
