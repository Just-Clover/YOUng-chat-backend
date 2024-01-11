package com.clover.youngchat.domain.chatroom.dto.response;

import com.clover.youngchat.domain.chat.entity.Chat;
import com.clover.youngchat.domain.chatroom.entity.ChatRoom;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomListGetRes {

    private String title;
    private String lastChat;
    private LocalDateTime lastChatTime;

    @Builder
    private ChatRoomListGetRes(String title, String lastChat, LocalDateTime lastChatTime) {
        this.title = title;
        this.lastChat = lastChat;
        this.lastChatTime = lastChatTime;
    }

    public ChatRoomListGetRes to(ChatRoom chatRoom, Chat chat) {
        return ChatRoomListGetRes.builder()
            .title(chatRoom.getTitle())
            .lastChat(chat.getMessage())
            .lastChatTime(chat.getCreatedAt())
            .build();
    }
}
