package com.clover.youngchat.domain.chatroom.repository;

import com.clover.youngchat.domain.chatroom.entity.QChatRoomUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatRoomUserRepositoryImpl implements ChatRoomUserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<Long>> getOtherUsersInChatRoom(Long chatRoomId, Long userId) {
        QChatRoomUser chatRoomUser = QChatRoomUser.chatRoomUser;

        return Optional.ofNullable(
            queryFactory.select(Projections.constructor(Long.class, chatRoomUser.user.id))
                .from(chatRoomUser)
                .where(chatRoomUser.chatRoom.id.in(chatRoomId))
                .groupBy(chatRoomUser.user.id)
                .having(chatRoomUser.user.id.notIn(userId))
                .fetch());
    }
}
