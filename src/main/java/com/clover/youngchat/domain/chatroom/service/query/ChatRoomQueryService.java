package com.clover.youngchat.domain.chatroom.service.query;

import static com.clover.youngchat.domain.chatroom.constant.ChatRoomConstant.CHAT_ROOM_LIMIT_SIZE;
import static com.clover.youngchat.global.exception.ResultCode.ACCESS_DENY;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_CHATROOM;

import com.clover.youngchat.domain.chat.repository.ChatRepository;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomAndLastChatGetRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomDetailGetRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomPaginationDetailGetRes;
import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomRepository;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomUserRepository;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import com.clover.youngchat.global.exception.GlobalException;
import com.clover.youngchat.global.response.RestSlice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomQueryService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public RestSlice<ChatRoomAndLastChatGetRes> getChatRoomList(User user) {
        return chatRoomUserRepository.findChatRoomsAndLastChatByUserId(user.getId(),
            CHAT_ROOM_LIMIT_SIZE);
    }

    public ChatRoomDetailGetRes getDetailChatRoom(Long chatRoomId, User user) {
        isChatRoomMember(chatRoomId, user.getId());
        ChatRoom chatRoom = findById(chatRoomId);

//        List<ChatRes> chatList = chatRepository.findAllByChatRoom_Id(chatRoomId)
//            .orElseThrow(() -> new GlobalException(NOT_FOUND_CHAT))
//            .stream()
//            .map(ChatRes::to)
//            .toList();

        return ChatRoomDetailGetRes.builder()
            .title(chatRoom.getTitle())
//            .chatResList(chatList)
            .build();
    }

    public ChatRoomPaginationDetailGetRes getPaginationDetailChatRoom(Long chatRoomId,
        Long lastChatId,
        User user) {
        isChatRoomMember(chatRoomId, user.getId());
        ChatRoom chatRoom = findById(chatRoomId);

//        RestSlice<ChatRes> chatResList =
//            chatRepository.findChatsByChatRoomId(chatRoomId, lastChatId,
//                CHAT_ROOM_DETAIL_LIMIT_SIZE);

        return ChatRoomPaginationDetailGetRes.builder()
            .title(chatRoom.getTitle())
//            .chatResList(chatResList)
            .build();
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
}
