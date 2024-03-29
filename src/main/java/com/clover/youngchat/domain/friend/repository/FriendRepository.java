package com.clover.youngchat.domain.friend.repository;

import com.clover.youngchat.domain.friend.entity.Friend;
import com.clover.youngchat.domain.friend.entity.FriendPK;
import com.clover.youngchat.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Friend.class, idClass = FriendPK.class)
public interface FriendRepository extends FriendRepositoryCustom {

    Optional<List<Friend>> findByUser(User user);

    Friend save(Friend friend);

    boolean existsByUser_IdAndFriend_Id(Long userId, Long friendId);

    void deleteByFriend_Id(Long friendId);
}
