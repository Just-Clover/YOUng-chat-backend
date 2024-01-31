package com.clover.youngchat.domain.chatroom.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomUserRepositoryCustom {

    Optional<List<Long>> getOtherUsersInChatRoom(Long chatRoomId, Long userId);
}
