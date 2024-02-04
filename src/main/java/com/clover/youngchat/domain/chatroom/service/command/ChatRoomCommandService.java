package com.clover.youngchat.domain.chatroom.service.command;

import static com.clover.youngchat.domain.chatroom.constant.ChatRoomConstant.COUNT_ONE_FRIEND;
import static com.clover.youngchat.domain.chatroom.constant.ChatRoomConstant.GROUP_CHATROOM_TITLE;
import static com.clover.youngchat.domain.chatroom.constant.ChatRoomConstant.PERSONAL_CHATROOM_TITLE;
import static com.clover.youngchat.global.exception.ResultCode.ACCESS_DENY;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_CHATROOM;

import com.clover.youngchat.domain.chatroom.dto.request.ChatRoomEditReq;
import com.clover.youngchat.domain.chatroom.dto.request.GroupChatRoomCreateReq;
import com.clover.youngchat.domain.chatroom.dto.request.PersonalChatRoomCreateReq;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomEditRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomLeaveRes;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserRepository userRepository;

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

    public GroupChatRoomCreateRes createGroupChatRoom(GroupChatRoomCreateReq req, User user) {
        List<User> participants = new ArrayList<>(req.getFriendIds().stream()
            .map(this::findByUserId).toList());
        participants.add(user);

        String title = String.format(GROUP_CHATROOM_TITLE,
            user.getUsername(), participants.size() - COUNT_ONE_FRIEND);
        ChatRoom chatRoom = saveChatRoom(title, participants);
        return GroupChatRoomCreateRes.to(chatRoom.getId(), chatRoom.getTitle());
    }

    public ChatRoomEditRes editChatRoom(Long chatRoomId, ChatRoomEditReq req, User user) {
        ChatRoom chatRoom = findById(chatRoomId);

        isChatRoomMember(chatRoomId, user.getId());

        chatRoom.updateChatRoom(req);

        return new ChatRoomEditRes();
    }

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

    private ChatRoom findById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(() ->
            new GlobalException(NOT_FOUND_CHATROOM));
    }

    private User findByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
            new GlobalException(ResultCode.NOT_FOUND_USER));
    }

    private void isChatRoomMember(Long chatRoomId, Long userId) {
        if (!chatRoomUserRepository.existsByChatRoom_IdAndUser_Id(chatRoomId, userId)) {
            throw new GlobalException(ACCESS_DENY);
        }
    }
}
