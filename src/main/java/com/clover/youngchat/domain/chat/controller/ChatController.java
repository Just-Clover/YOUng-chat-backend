package com.clover.youngchat.domain.chat.controller;

import com.clover.youngchat.domain.chat.dto.request.ChatCreateReq;
import com.clover.youngchat.domain.chat.dto.request.ChatDeleteReq;
import com.clover.youngchat.domain.chat.service.command.ChatCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatCommandService chatCommandService;

    @MessageMapping("chat-rooms.{chatRoomId}")
    public void sendMessage(@DestinationVariable Long chatRoomId,
        @Payload ChatCreateReq chatCreateReq) {
        chatCommandService.sendMessage(chatRoomId, chatCreateReq);
    }

    @MessageMapping("chat-rooms.{chatRoomId}.delete")
    public void deleteChat(@DestinationVariable Long chatRoomId,
        @Payload ChatDeleteReq chatDeleteReq) {
        chatCommandService.deleteChat(chatRoomId, chatDeleteReq);
    }
}
