package com.clover.youngchat.domain.chatroom.repository;

import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import com.clover.youngchat.domain.chatroom.entity.QChatRoomUser;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatRoomUserRepositoryImpl implements ChatRoomUserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ChatRoom> findChatRoomIdByOnlyTwoUsers(final Long userId, final Long friendId) {
        QChatRoomUser chatRoomUser = QChatRoomUser.chatRoomUser;
        QChatRoomUser chatRoomUserSub = new QChatRoomUser("chatRoomUserSub");

        // 서브쿼리: 두 사용자가 모두 있는 채팅방 ID 찾기
        JPQLQuery<Long> subQuery = JPAExpressions
            .select(chatRoomUserSub.chatRoom.id)
            .from(chatRoomUserSub)
            .where(chatRoomUserSub.user.id.in(userId, friendId))
            .groupBy(chatRoomUserSub.chatRoom.id)
            .having(chatRoomUserSub.user.id.count().eq(2L));

        // 메인 쿼리: 서브쿼리 결과를 이용하여 ChatRoom 찾기
        ChatRoom chatRoom = queryFactory
            .select(chatRoomUser.chatRoom)
            .from(chatRoomUser)
            .where(chatRoomUser.chatRoom.id.in(subQuery))
            .groupBy(chatRoomUser.chatRoom.id)
            .having(chatRoomUser.user.id.count().eq(2L))
            .fetchOne();

        return Optional.ofNullable(chatRoom);
    }
}
