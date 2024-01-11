package com.clover.youngchat.domain.chat.service;

import static com.clover.youngchat.global.exception.ResultCode.ACCESS_DENY;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_CHATROOM;

import com.clover.youngchat.domain.chat.dto.request.ChatCreateReq;
import com.clover.youngchat.domain.chat.dto.response.ChatCreateRes;
import com.clover.youngchat.domain.chat.entity.Chat;
import com.clover.youngchat.domain.chat.repository.ChatRepository;
import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import com.clover.youngchat.domain.chatroom.entity.ChatRoomUser;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomRepository;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomUserRepository;
import com.clover.youngchat.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    public ChatCreateRes createChat(
        final Long chatRoomId, final ChatCreateReq req, final Long userId) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_CHATROOM));

        ChatRoomUser chatRoomUser = chatRoomUserRepository.findByChatRoom_IdAndUser_Id(chatRoomId,
                userId)
            .orElseThrow(() -> new GlobalException(ACCESS_DENY));

        Chat chat = Chat.builder()
            .message(req.getMessage())
            .sender(chatRoomUser.getUser())
            .chatRoom(chatRoom)
            .build();

        chatRepository.save(chat);
        return new ChatCreateRes();
    }
}
