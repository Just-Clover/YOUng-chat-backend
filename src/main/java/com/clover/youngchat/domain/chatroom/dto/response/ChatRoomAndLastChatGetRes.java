package com.clover.youngchat.domain.chatroom.dto.response;

import com.clover.youngchat.domain.chat.entity.Chat;
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
    private String chatId;
    private String lastChat;
    private LocalDateTime lastChatTime;
    private boolean lastChatDeleted;

    @Builder
    public ChatRoomAndLastChatGetRes(Long chatRoomId, String title, String chatId, String lastChat,
        LocalDateTime lastChatTime, boolean lastChatDeleted) {
        this.chatRoomId = chatRoomId;
        this.title = title;
        this.chatId = chatId;
        this.lastChat = lastChat;
        this.lastChatTime = lastChatTime;
        this.lastChatDeleted = lastChatDeleted;
    }


    public static ChatRoomAndLastChatGetRes to(String title, Chat chat) {
        return ChatRoomAndLastChatGetRes.builder()
            .chatRoomId(chat.getChatRoomId())
            .title(title)
            .chatId(chat.getId())
            .lastChatDeleted(chat.isDeleted())
            .lastChat(chat.getMessage())
            .lastChatTime(chat.getCreatedAt())
            .build();
    }
}
