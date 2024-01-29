package com.clover.youngchat.domain.chatroom.repository;

import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomUserRepositoryCustom {

    Optional<ChatRoom> findChatRoomIdByOnlyTwoUsers(Long userId1, Long userId2);

    Optional<List<Long>> getOtherUsersInChatRoom(Long chatRoomId, Long userId);
}
