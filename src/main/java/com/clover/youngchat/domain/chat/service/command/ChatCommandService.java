package com.clover.youngchat.domain.chat.service.command;

import static com.clover.youngchat.global.exception.ResultCode.ACCESS_DENY;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_CHAT;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_CHATROOM;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_USER;

import com.clover.youngchat.domain.chat.dto.request.ChatCreateReq;
import com.clover.youngchat.domain.chat.dto.response.ChatDeleteRes;
import com.clover.youngchat.domain.chat.dto.response.ChatRes;
import com.clover.youngchat.domain.chat.entity.Chat;
import com.clover.youngchat.domain.chat.repository.ChatRepository;
import com.clover.youngchat.domain.chatroom.dto.response.ChatAlertRes;
import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import com.clover.youngchat.domain.chatroom.entity.ChatRoomUser;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomRepository;
import com.clover.youngchat.domain.chatroom.repository.ChatRoomUserRepository;
import com.clover.youngchat.domain.user.entity.User;
import com.clover.youngchat.domain.user.repository.UserRepository;
import com.clover.youngchat.global.exception.GlobalException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatCommandService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    public void sendMessage(Long chatRoomId, ChatCreateReq req) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_CHATROOM));

        User user = userRepository.findById(req.getUserId())
            .orElseThrow(() -> new GlobalException(NOT_FOUND_USER));

        ChatRoomUser chatRoomUser = chatRoomUserRepository.findByChatRoom_IdAndUser_Id(chatRoomId,
                user.getId())
            .orElseThrow(() -> new GlobalException(ACCESS_DENY));

        Chat chat = chatRepository.save(Chat.builder()
            .message(req.getMessage())
            .sender(chatRoomUser.getUser())
            .chatRoom(chatRoom)
            .build());

        rabbitTemplate.convertAndSend(exchangeName, "chat-rooms." + chatRoomId, ChatRes.to(chat));

        List<Long> userIds = getUserIdListByChatRoomId(chatRoomId, user.getId());

        ChatAlertRes res = ChatAlertRes.to(chatRoomId, chatRoom.getTitle(), user.getUsername(),
            user.getProfileImage(), req.getMessage());

        userIds.forEach(userId -> {
            String routingKey = "users." + userId;
            rabbitTemplate.convertAndSend(exchangeName, routingKey, res);
        });

        log.info("Message [{}] send by member: {} to chatting room: {}", req.getMessage(),
            user.getId(),
            chatRoom.getId());
    }

    public ChatDeleteRes deleteChat(
        final Long chatRoomId, final Long chatId, final Long userId) {
        if (!chatRoomUserRepository.existsByChatRoom_IdAndUser_Id(chatRoomId, userId)) {
            throw new GlobalException(ACCESS_DENY);
        }
        Chat chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_CHAT));

        chat.deleteChat();

        return new ChatDeleteRes();
    }

    private List<Long> getUserIdListByChatRoomId(Long chatRoomId, Long userId) {
        return chatRoomUserRepository.getOtherUsersInChatRoom(chatRoomId, userId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_CHATROOM));
    }
}