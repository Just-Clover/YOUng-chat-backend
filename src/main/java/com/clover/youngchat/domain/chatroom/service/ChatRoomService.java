package com.clover.youngchat.domain.chatroom.service;


import static com.clover.youngchat.global.exception.ResultCode.ACCESS_DENY;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_CHAT;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_CHATROOM;

import com.clover.youngchat.domain.chat.entity.Chat;
import com.clover.youngchat.domain.chat.repository.ChatRepository;
import com.clover.youngchat.domain.chatroom.dto.request.ChatRoomCreateReq;
import com.clover.youngchat.domain.chatroom.dto.request.ChatRoomEditReq;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomCreateRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomEditRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomLeaveRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomListGetRes;
import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import com.clover.youngchat.domain.chatroom.entity.ChatRoomUser;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomRepository;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomUserRepository;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import com.clover.youngchat.global.exception.GlobalException;
import com.clover.youngchat.global.exception.ResultCode;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatRoomCreateRes createChatRoom(ChatRoomCreateReq req, User user) {
        User friend = userRepository.findById(req.getFriendId()).orElseThrow(() ->
            new GlobalException(ResultCode.NOT_FOUND_USER));

        ChatRoom chatRoom = ChatRoom.builder()
            .title(req.getTitle())
            .build();

        chatRoomRepository.save(chatRoom);

        ChatRoomUser myChat = ChatRoomUser.builder()
            .user(user)
            .chatRoom(chatRoom)
            .build();

        chatRoomUserRepository.save(myChat);

        ChatRoomUser friendChat = ChatRoomUser.builder()
            .user(friend)
            .chatRoom(chatRoom)
            .build();

        chatRoomUserRepository.save(friendChat);

        return new ChatRoomCreateRes();
    }

    @Transactional
    public ChatRoomEditRes editChatRoom(Long chatRoomId, ChatRoomEditReq req, User user) {
        ChatRoom chatRoom = findById(chatRoomId);

        isChatRoomMember(chatRoomId, user.getId());

        chatRoom.updateChatRoom(req);

        return new ChatRoomEditRes();
    }

    @Transactional
    public ChatRoomLeaveRes leaveChatRoom(Long chatRoomId, User user) {
        if (!chatRoomRepository.existsById(chatRoomId)) {
            throw new GlobalException(NOT_FOUND_CHATROOM);
        }

        ChatRoomUser chatRoomUser = chatRoomUserRepository.findByChatRoom_IdAndUser_Id(chatRoomId,
                user.getId())
            .orElseThrow(() -> new GlobalException(ACCESS_DENY));

        chatRoomUserRepository.delete(chatRoomUser);

        return new ChatRoomLeaveRes();
    }

    private ChatRoom findById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(() ->
            new GlobalException(NOT_FOUND_CHATROOM));
    }

    private void isChatRoomMember(Long chatRoomId, Long userId) {
        if (!chatRoomUserRepository.existsByChatRoom_IdAndUser_Id(chatRoomId, userId)) {
            throw new GlobalException(ACCESS_DENY);
        }
    }

    @Transactional
    public List<ChatRoomListGetRes> getChatRoomList(User user) {
        List<ChatRoomUser> chatRoomUserList = chatRoomUserRepository.findByUser_Id(user.getId())
            .orElseThrow(() ->
                new GlobalException(NOT_FOUND_CHATROOM));

        List<ChatRoomListGetRes> getResList = new ArrayList<>();

        for (ChatRoomUser c : chatRoomUserList) {
            Chat chat = chatRepository.findByChatRoom_Id(c.getChatRoom().getId())
                .orElseThrow(() -> new GlobalException(NOT_FOUND_CHAT));
            ChatRoomListGetRes crs = ChatRoomListGetRes.builder().build();
            getResList.add(crs.to(c.getChatRoom(), chat));
        }

        return getResList;
    }
}
