package com.clover.youngchat.domain.friend.repository;

import com.clover.youngchat.domain.friend.dto.response.FriendGetSearchListRes;
import com.clover.youngchat.domain.friend.entity.QFriend;
import com.clover.youngchat.domain.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FriendGetSearchListRes> findFriendByKeyword(Long userId, String keyword) {
        QUser user = QUser.user;
        QFriend friend = QFriend.friend1;

        return queryFactory.select(
                Projections.constructor(FriendGetSearchListRes.class,
                    user.id, user.username, user.profileImage))
            .from(user)
            .where(
                user.id.in(
                    JPAExpressions
                        .select(friend.friend.id)
                        .from(friend)
                        .where(friend.user.id.eq(userId))
                ),
                user.username.containsIgnoreCase(keyword)
            )
            .fetch();
    }
}
