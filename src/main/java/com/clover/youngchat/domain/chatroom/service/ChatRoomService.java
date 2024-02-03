package com.clover.youngchat.domain.chatroom.service;


import static com.clover.youngchat.domain.chatroom.constant.ChatRoomConstant.CHAT_ROOM_CACHE;
import static com.clover.youngchat.domain.chatroom.constant.ChatRoomConstant.CHAT_ROOM_DETAIL_LIMIT_SIZE;
import static com.clover.youngchat.domain.chatroom.constant.ChatRoomConstant.CHAT_ROOM_LIMIT_SIZE;
import static com.clover.youngchat.domain.chatroom.constant.ChatRoomConstant.COUNT_ONE_FRIEND;
import static com.clover.youngchat.domain.chatroom.constant.ChatRoomConstant.GROUP_CHATROOM_TITLE;
import static com.clover.youngchat.domain.chatroom.constant.ChatRoomConstant.PERSONAL_CHATROOM_TITLE;
import static com.clover.youngchat.global.exception.ResultCode.ACCESS_DENY;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_CHAT;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_CHATROOM;

import com.clover.youngchat.domain.chat.dto.response.ChatRes;
import com.clover.youngchat.domain.chat.repository.ChatRepository;
import com.clover.youngchat.domain.chatroom.dto.request.ChatRoomEditReq;
import com.clover.youngchat.domain.chatroom.dto.request.GroupChatRoomCreateReq;
import com.clover.youngchat.domain.chatroom.dto.request.PersonalChatRoomCreateReq;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomAndLastChatGetRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomDetailGetRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomEditRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomLeaveRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomPaginationDetailGetRes;
import com.clover.youngchat.domain.chatroom.dto.response.GroupChatRoomCreateRes;
import com.clover.youngchat.domain.chatroom.dto.response.PersonalChatRoomCreateRes;
import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import com.clover.youngchat.domain.chatroom.entity.ChatRoomUser;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomRepository;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomUserRepository;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import com.clover.youngchat.global.exception.GlobalException;
import com.clover.youngchat.global.exception.ResultCode;
import com.clover.youngchat.global.response.RestSlice;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Transactional
    public PersonalChatRoomCreateRes createPersonalChatRoom(
        PersonalChatRoomCreateReq req, User user) {
        User friend = findByUserId(req.getFriendId());

        ChatRoom chatRoom = chatRoomUserRepository
            .findChatRoomByOnlyTwoUsers(user.getId(), friend.getId())
            .orElseGet(() -> {
                String title = String.format(PERSONAL_CHATROOM_TITLE, user.getUsername(),
                    friend.getUsername());
                return saveChatRoom(title, Arrays.asList(user, friend));
            });
        return PersonalChatRoomCreateRes.to(chatRoom.getId(), chatRoom.getTitle());
    }

    @Transactional
    public GroupChatRoomCreateRes createGroupChatRoom(GroupChatRoomCreateReq req, User user) {
        List<User> participants = new ArrayList<>(req.getFriendIds().stream()
            .map(this::findByUserId).toList());
        participants.add(user);

        String title = String.format(GROUP_CHATROOM_TITLE,
            user.getUsername(), participants.size() - COUNT_ONE_FRIEND);
        ChatRoom chatRoom = saveChatRoom(title, participants);
        return GroupChatRoomCreateRes.to(chatRoom.getId(), chatRoom.getTitle());
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

    @Transactional(readOnly = true)
    public RestSlice<ChatRoomAndLastChatGetRes> getChatRoomList(User user, Long cursorChatId) {
        userRepository.findById(user.getId());
        return chatRoomUserRepository.findChatRoomsAndLastChatByUserId(user.getId(),
            cursorChatId, CHAT_ROOM_LIMIT_SIZE);
    }

    @Transactional(readOnly = true)
    public ChatRoomDetailGetRes getDetailChatRoom(Long chatRoomId, User user) {
        // 채팅방에 속한 사람만 조회 가능
        isChatRoomMember(chatRoomId, user.getId());
        ChatRoom chatRoom = findById(chatRoomId);

        List<ChatRes> chatList = chatRepository.findAllByChatRoom_Id(chatRoomId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_CHAT))
            .stream()
            .map(ChatRes::to)
            .toList();

        return ChatRoomDetailGetRes.builder()
            .title(chatRoom.getTitle())
            .chatResList(chatList)
            .build();
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CHAT_ROOM_CACHE, key = "#chatRoomId.toString().concat(':').concat(#lastChatId)", cacheManager = "cacheManager")
    public ChatRoomPaginationDetailGetRes getPaginationDetailChatRoom(Long chatRoomId,
        Long lastChatId,
        User user) {
        // 채팅방에 속한 사람만 조회 가능
        isChatRoomMember(chatRoomId, user.getId());
        ChatRoom chatRoom = findById(chatRoomId);

        RestSlice<ChatRes> chatResList =
            chatRepository.findChatsByChatRoomId(chatRoomId, lastChatId,
                CHAT_ROOM_DETAIL_LIMIT_SIZE);

        return ChatRoomPaginationDetailGetRes.builder()
            .title(chatRoom.getTitle())
            .chatResList(chatResList)
            .build();
    }

    private ChatRoom findById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(() ->
            new GlobalException(NOT_FOUND_CHATROOM));
    }

    private User findByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
            new GlobalException(ResultCode.NOT_FOUND_USER));
    }

    @Transactional
    protected ChatRoom saveChatRoom(String title, List<User> participants) {
        ChatRoom chatRoom = ChatRoom.builder()
            .title(title)
            .build();
        ChatRoom saveChatRoom = chatRoomRepository.save(chatRoom);

        List<ChatRoomUser> chatRoomUsers = new ArrayList<>();
        for (User participant : participants) {
            chatRoomUsers.add(ChatRoomUser.to(participant, saveChatRoom));
        }
        chatRoomUserRepository.saveAll(chatRoomUsers);

        return chatRoom;
    }

    private void isChatRoomMember(Long chatRoomId, Long userId) {
        if (!chatRoomUserRepository.existsByChatRoom_IdAndUser_Id(chatRoomId, userId)) {
            throw new GlobalException(ACCESS_DENY);
        }
    }
}
