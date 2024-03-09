package com.clover.youngchat.domain.chatroom.repository;

import com.clover.youngchat.domain.chat.entity.Chat;
import com.clover.youngchat.domain.chat.repository.ChatRepository;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomAndLastChatGetRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomIdAndTitleGetRes;
import com.clover.youngchat.domain.chatroom.entity.QChatRoom;
import com.clover.youngchat.domain.chatroom.entity.QChatRoomUser;
import com.clover.youngchat.global.response.RestSlice;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatRoomUserRepositoryImpl implements ChatRoomUserRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final ChatRepository chatRepository;

//    @Override
//    public Optional<List<Long>> getOtherUsersInChatRoom(Long chatRoomId, Long userId) {
//        QChatRoomUser chatRoomUser = QChatRoomUser.chatRoomUser;
//
//        return Optional.ofNullable(
//            queryFactory.select(Projections.constructor(Long.class, chatRoomUser.user.id))
//                .from(chatRoomUser)
//                .where(chatRoomUser.chatRoom.id.in(chatRoomId))
//                .groupBy(chatRoomUser.user.id)
//                .having(chatRoomUser.user.id.notIn(userId))
//                .fetch());
//    }

    @Override
    public RestSlice<ChatRoomAndLastChatGetRes> findChatRoomsAndLastChatByUserId(Long userId,
        int limitSize) {
        List<ChatRoomIdAndTitleGetRes> resList = queryChatRooms(userId, limitSize);
        // 문제 1. 채팅방 ID가 있지만 내가 하고 싶은 것은 최근 채팅 별로 정렬하고 싶음
        List<ChatRoomAndLastChatGetRes> list = new ArrayList<>();
        for (ChatRoomIdAndTitleGetRes res : resList) {
            Chat chat = chatRepository.findFirstByChatRoomIdOrderByCreatedAtAsc(res.getChatRoomId())
                .get();
            list.add(ChatRoomAndLastChatGetRes.to(res.getTitle(), chat));
        }

        return createSlice(list, limitSize);
    }

    private List<ChatRoomIdAndTitleGetRes> queryChatRooms(Long userId, int limitSize) {
        QChatRoomUser qChatRoomUser = QChatRoomUser.chatRoomUser;
        QChatRoom qChatRoom = QChatRoom.chatRoom;

        return queryFactory
            .select(
                Projections.constructor(ChatRoomIdAndTitleGetRes.class,
                    qChatRoom.id,
                    qChatRoom.title
                ))
            .from(qChatRoomUser)
            .join(qChatRoom).on(qChatRoomUser.chatRoom.id.eq(qChatRoom.id))
            .where(qChatRoomUser.user.id.eq(userId))
            .limit(limitSize)
            .fetch();
    }

//    private BooleanExpression createQueryCondition(QChat qChat, Long cursorChatId) {
//        BooleanExpression condition = qChat.id.gt(1L);
//        if (cursorChatId != null) {
//            condition = condition.and(qChat.id.lt(cursorChatId));
//        }
//        return condition;
//    }

    private RestSlice<ChatRoomAndLastChatGetRes> createSlice(List<ChatRoomAndLastChatGetRes> res,
        int limitSize) {
        boolean hasNext = res.size() > limitSize;
        if (hasNext) {
            res.remove(0);
        }
        return new RestSlice<>(res, 0, limitSize, hasNext);
    }
}