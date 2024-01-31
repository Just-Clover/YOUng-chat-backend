package com.clover.youngchat.domain.chatroom.repository;

import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomAndLastChatGetRes;
import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import java.util.Optional;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomUserRepositoryCustom {

    Optional<ChatRoom> findChatRoomIdByOnlyTwoUsers(Long userId1, Long userId2);

    Slice<ChatRoomAndLastChatGetRes> findChatRoomsAndLastChatByUserId(Long userId, Long cursorChatRoomId, int limitSize);
}
