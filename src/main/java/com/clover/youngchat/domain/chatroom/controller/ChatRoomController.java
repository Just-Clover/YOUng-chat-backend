package com.clover.youngchat.domain.chatroom.controller;

import com.clover.youngchat.domain.chatroom.dto.request.ChatRoomCreateReq;
import com.clover.youngchat.domain.chatroom.dto.request.ChatRoomEditReq;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomAndLastChatGetRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomCreateRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomDetailGetRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomEditRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomLeaveRes;
import com.clover.youngchat.domain.chatroom.dto.response.ChatRoomPaginationDetailGetRes;
import com.clover.youngchat.domain.chatroom.service.ChatRoomService;
import com.clover.youngchat.global.response.RestResponse;
import com.clover.youngchat.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat-rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public RestResponse<ChatRoomCreateRes> createChatRoom(@RequestBody ChatRoomCreateReq req,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return RestResponse.success(
            chatRoomService.createChatRoom(req, userDetails.getUser()));
    }

    @GetMapping
    public RestResponse<List<ChatRoomAndLastChatGetRes>> getChatRoomList(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return RestResponse.success(chatRoomService.getChatRoomList(userDetails.getUser()));
    }

    @GetMapping("/{chatRoomId}")
    public RestResponse<ChatRoomDetailGetRes> getDetailChatRoom(@PathVariable Long chatRoomId,
        @AuthenticationPrincipal UserDetailsImpl
            userDetails) {
        return RestResponse.success(
            chatRoomService.getDetailChatRoom(chatRoomId, userDetails.getUser()));
    }

    @GetMapping("/slice/{chatRoomId}")
    public RestResponse<ChatRoomPaginationDetailGetRes> getPaginationDetailChatRoom(
        @PathVariable Long chatRoomId,
        @RequestParam(required = false) Long lastChatId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return RestResponse.success(
            chatRoomService.getPaginationDetailChatRoom(chatRoomId, lastChatId,
                userDetails.getUser())
        );
    }

    @PatchMapping("/{chatRoomId}")
    public RestResponse<ChatRoomEditRes> editChatRoom(@PathVariable Long chatRoomId,
        @RequestBody @Valid ChatRoomEditReq req,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return RestResponse.success(
            chatRoomService.editChatRoom(chatRoomId, req, userDetails.getUser()));
    }

    @DeleteMapping("/{chatRoomId}")
    public RestResponse<ChatRoomLeaveRes> leaveChatRoom(@PathVariable Long chatRoomId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return RestResponse.success(
            chatRoomService.leaveChatRoom(chatRoomId, userDetails.getUser()));
    }
}
