package com.clover.youngchat.domain.chat.controller;

import com.clover.youngchat.domain.chat.dto.request.ChatCreateReq;
import com.clover.youngchat.domain.chat.dto.response.ChatDeleteRes;
import com.clover.youngchat.domain.chat.dto.response.ChatRes;
import com.clover.youngchat.domain.chat.service.ChatService;
import com.clover.youngchat.global.response.RestResponse;
import com.clover.youngchat.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat-rooms/{chatRoomId}/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @SendTo("/sub/chat-rooms/{chatRoomId}/chats")
    @MessageMapping("/chat-rooms/{chatRoomId}/chats")
    public RestResponse<ChatRes> sendMessage(@DestinationVariable Long chatRoomId,
        @Payload ChatCreateReq chatCreateReq) {
        return RestResponse.success(chatService.sendMessage(chatRoomId, chatCreateReq));
    }

    @DeleteMapping("/{chatId}")
    public RestResponse<ChatDeleteRes> deleteChat(
        @PathVariable Long chatRoomId, @PathVariable Long chatId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return RestResponse.success(
            chatService.deleteChat(chatRoomId, chatId, userDetails.getUser().getId()));
    }

}
