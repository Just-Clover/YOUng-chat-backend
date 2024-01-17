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
public class ChatRoomAndLastChatGetRes {

    private Long chatRoomId;
    private String title;
    private String lastChat;
    private LocalDateTime lastChatTime;

    @Builder
    private ChatRoomAndLastChatGetRes(Long chatRoomId, String title, String lastChat,
        LocalDateTime lastChatTime) {
        this.chatRoomId = chatRoomId;
        this.title = title;
        this.lastChat = lastChat;
        this.lastChatTime = lastChatTime;
    }

    public static ChatRoomAndLastChatGetRes to(ChatRoom chatRoom, Chat chat) {
        return ChatRoomAndLastChatGetRes.builder()
            .chatRoomId(chatRoom.getId())
            .title(chatRoom.getTitle())
            .lastChat((chat == null) ? "" : chat.getMessage())
            .lastChatTime((chat == null) ? chatRoom.getCreatedAt() : chat.getCreatedAt())
            .build();
    }
}
