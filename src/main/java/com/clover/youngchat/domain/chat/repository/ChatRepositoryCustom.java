package com.clover.youngchat.domain.chat.repository;


import com.clover.youngchat.domain.chat.dto.response.ChatRes;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepositoryCustom {

    Slice<ChatRes> findChatsByChatRoomId(Long chatRoomId, Long cursorChatId, int limitSize);

}