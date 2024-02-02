package com.clover.youngchat.domain.chatroom.repository;

import com.clover.youngchat.domain.chat.entity.QChat;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomAndLastChatGetRes;
import com.clover.youngchat.domain.chatroom.entity.QChatRoom;
import com.clover.youngchat.domain.chatroom.entity.QChatRoomUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

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

    @Override
    public Slice<ChatRoomAndLastChatGetRes> findChatRoomsAndLastChatByUserId(Long userId,
        Long cursorChatId, int limitSize) {
        List<ChatRoomAndLastChatGetRes> resList = queryChatRooms(userId, cursorChatId,
            limitSize);
        return createSlice(resList, limitSize);
    }

    private List<ChatRoomAndLastChatGetRes> queryChatRooms(Long userId, Long cursorChatRoomId,
        int limitSize) {
        QChatRoomUser qChatRoomUser = QChatRoomUser.chatRoomUser;
        QChatRoom qChatRoom = QChatRoom.chatRoom;
        QChat qChat = QChat.chat;
        BooleanExpression queryCondition = createQueryCondition(qChat, cursorChatRoomId);

        return queryFactory
            .select(
                Projections.constructor(ChatRoomAndLastChatGetRes.class,
                    qChatRoom.id,
                    qChatRoom.title,
                    qChat.id,
                    qChat.message,
                    qChat.createdAt,
                    qChat.isDeleted))
            .from(qChatRoomUser)
            .join(qChatRoom).on(qChatRoomUser.chatRoom.id.eq(qChatRoom.id))
            .join(qChat).on(qChat.chatRoom.id.eq(qChatRoom.id))
            .where(qChatRoomUser.user.id.eq(userId))
            .where(Expressions.list(qChatRoom.id, qChat.createdAt).in(
                JPAExpressions
                    .select(qChat.chatRoom.id, qChat.createdAt.max())
                    .from(qChat)
                    .groupBy(qChat.chatRoom.id)))
            .where(queryCondition)
            .orderBy(qChat.id.desc())
            .limit(limitSize)
            .fetch();
    }

    private BooleanExpression createQueryCondition(QChat qChat, Long cursorChatId) {
        BooleanExpression condition = qChat.id.gt(1L);
        if (cursorChatId != null) {
            condition = condition.and(qChat.id.lt(cursorChatId));
        }
        return condition;
    }

    private Slice<ChatRoomAndLastChatGetRes> createSlice(List<ChatRoomAndLastChatGetRes> res,
        int limitSize) {
        boolean hasNext = res.size() > limitSize;
        if (hasNext) {
            res.remove(0);
        }
        return new SliceImpl<>(res, PageRequest.of(0, limitSize), hasNext);
    }
}