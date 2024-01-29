package com.clover.youngchat.domain.chatroom.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatAlertRes {

    private String chatRoomName;
    private String username;
    private String profileImage;
    private String message;

    @Builder
    private ChatAlertRes(String chatRoomName, String username, String profileImage,
        String message) {
        this.chatRoomName = chatRoomName;
        this.username = username;
        this.profileImage = profileImage;
        this.message = message;
    }

    public static ChatAlertRes to(String chatRoomName, String username, String profileImage,
        String message) {
        return ChatAlertRes.builder()
            .chatRoomName(chatRoomName)
            .username(username)
            .profileImage(profileImage)
            .message(message)
            .build();
    }
}
