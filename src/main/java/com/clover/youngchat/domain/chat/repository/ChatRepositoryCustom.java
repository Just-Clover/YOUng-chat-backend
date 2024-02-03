package com.clover.youngchat.domain.chat.repository;


import com.clover.youngchat.domain.chat.dto.response.ChatRes;
import com.clover.youngchat.global.response.RestSlice;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepositoryCustom {

    RestSlice<ChatRes> findChatsByChatRoomId(Long chatRoomId, Long cursorChatId, int limitSize);

}