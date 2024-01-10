package com.clover.youngchat.domain.chatroom.controller;

import com.clover.youngchat.domain.chatroom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
}
