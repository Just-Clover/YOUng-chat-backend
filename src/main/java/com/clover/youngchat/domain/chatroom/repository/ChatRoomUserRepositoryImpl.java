package com.clover.youngchat.domain.chatroom.repository;

import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import com.clover.youngchat.domain.chatroom.entity.QChatRoomUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatRoomUserRepositoryImpl implements ChatRoomUserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ChatRoom> findChatRoomIdByOnlyTwoUsers(final Long userId1, final Long userId2) {
        QChatRoomUser chatRoomUser = QChatRoomUser.chatRoomUser;

        ChatRoom chatRoom = queryFactory
            .select(chatRoomUser.chatRoom)
            .from(chatRoomUser)
            .where(chatRoomUser.user.id.in(userId1, userId2))
            .groupBy(chatRoomUser.chatRoom.id)
            .having(chatRoomUser.user.count().eq(2L))
            .fetchOne();

        return Optional.ofNullable(chatRoom);
    }
}
