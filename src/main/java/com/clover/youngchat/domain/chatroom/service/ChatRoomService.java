package com.clover.youngchat.domain.chatroom.service;


import static com.clover.youngchat.global.exception.ResultCode.ACCESS_DENY;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_CHAT;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_CHATROOM;

import com.clover.youngchat.domain.chat.dto.response.ChatRes;
import com.clover.youngchat.domain.chat.entity.Chat;
import com.clover.youngchat.domain.chat.repository.ChatRepository;
import com.clover.youngchat.domain.chatroom.dto.request.ChatRoomCreateReq;
import com.clover.youngchat.domain.chatroom.dto.request.ChatRoomEditReq;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomAndLastChatGetRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomCreateRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomDetailGetRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomEditRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomLeaveRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomPaginationDetailGetRes;
import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import com.clover.youngchat.domain.chatroom.entity.ChatRoomUser;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomRepository;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomUserRepository;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import com.clover.youngchat.global.exception.GlobalException;
import com.clover.youngchat.global.exception.ResultCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private static final int LIMIT_SIZE = 10;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatRoomCreateRes createChatRoom(ChatRoomCreateReq req, User user) {
        List<User> friends = req.getFriendIds().stream()
            .map(
                friendId -> userRepository.findById(friendId).orElseThrow(()
                    -> new GlobalException(ResultCode.NOT_FOUND_USER))
            ).toList();

        ChatRoom chatRoom;

        // 1:1 채팅일 경우
        if (friends.size() == 1) {
            chatRoom = chatRoomUserRepository
                .findChatRoomIdByOnlyTwoUsers(user.getId(), friends.get(0).getId())
                .orElseGet(()
                    -> saveChatRoom(setChatRoomTitle(user, friends), user, friends));
        } else {
            String title =
                req.getTitle().isBlank() ? setChatRoomTitle(user, friends) : req.getTitle();
            chatRoom = saveChatRoom(title, user, friends);
        }

        return ChatRoomCreateRes.to(chatRoom.getId(), chatRoom.getTitle());
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
    public List<ChatRoomAndLastChatGetRes> getChatRoomList(User user) {
        List<ChatRoomUser> chatRoomUserList = chatRoomUserRepository.findByUser_Id(user.getId())
            .orElseThrow(() ->
                new GlobalException(NOT_FOUND_CHATROOM));

        List<ChatRoomAndLastChatGetRes> getResList = new ArrayList<>();

        // TODO: 차후 로직 수정요함.
        for (ChatRoomUser c : chatRoomUserList) {
            Chat chat = chatRepository.findLastChatByChatRoom_Id(c.getChatRoom().getId())
                .orElse(null);

            ChatRoomAndLastChatGetRes res = ChatRoomAndLastChatGetRes.to(c.getChatRoom(), chat);
            getResList.add(res);
        }

        return getResList;
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
    public ChatRoomPaginationDetailGetRes getPaginationDetailChatRoom(Long chatRoomId,
        Long lastChatId,
        User user) {
        // 채팅방에 속한 사람만 조회 가능
        isChatRoomMember(chatRoomId, user.getId());
        ChatRoom chatRoom = findById(chatRoomId);

        Slice<ChatRes> chatResList =
            chatRepository.findChatsByChatRoomId(chatRoomId, lastChatId, LIMIT_SIZE);

        return ChatRoomPaginationDetailGetRes.builder()
            .title(chatRoom.getTitle())
            .chatResList(chatResList)
            .build();
    }

    private ChatRoom findById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(() ->
            new GlobalException(NOT_FOUND_CHATROOM));
    }

    private String setChatRoomTitle(User user, List<User> friends) {
        if (friends.size() == 1) {
            return String.format("%s, %s의 채팅방", user.getUsername(), friends.get(0).getUsername());
        }
        return String.format("%s, %s 외 %d 명",
            user.getUsername(), friends.get(0).getUsername(), friends.size() - 1);
    }

    @Transactional
    protected ChatRoom saveChatRoom(String title, User user, List<User> friends) {
        ChatRoom chatRoom = ChatRoom.builder()
            .title(title)
            .build();
        chatRoomRepository.save(chatRoom);

        List<ChatRoomUser> chatRoomUsers = new ArrayList<>();
        chatRoomUsers.add(ChatRoomUser.to(user, chatRoom));

        for (User friend : friends) {
            chatRoomUsers.add(ChatRoomUser.to(friend, chatRoom));
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
