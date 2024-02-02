package com.clover.youngchat.domain.chatroom.repository;

import java.util.List;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomAndLastChatGetRes;
import java.util.Optional;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomUserRepositoryCustom {

    Optional<List<Long>> getOtherUsersInChatRoom(Long chatRoomId, Long userId);

    Slice<ChatRoomAndLastChatGetRes> findChatRoomsAndLastChatByUserId(Long userId, Long cursorChatRoomId, int limitSize);
}
