package com.clover.youngchat.domain.chatroom.repository;

import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomAndLastChatGetRes;
import com.clover.youngchat.global.response.RestSlice;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomUserRepositoryCustom {

    Optional<List<Long>> getOtherUsersInChatRoom(Long chatRoomId, Long userId);

    RestSlice<ChatRoomAndLastChatGetRes> findChatRoomsAndLastChatByUserId(Long userId,
        Long cursorChatRoomId, int limitSize);
}
