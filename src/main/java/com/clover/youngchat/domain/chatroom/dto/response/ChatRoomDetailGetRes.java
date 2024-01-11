package com.clover.youngchat.domain.chatroom.dto.response;

import com.clover.youngchat.domain.chat.entity.Chat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomDetailGetRes {

    private String title;
    private List<ChatRes> chatResList;

    @Builder
    private ChatRoomDetailGetRes(String title) {
        this.title = title;
        this.chatResList = new ArrayList<>();
    }

    public void addChatRes(ChatRes chatRes) {
        this.chatResList.add(chatRes);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ChatRes {

        String message;
        String username;
        LocalDateTime messageTime;

        @Builder
        private ChatRes(String message, String username, LocalDateTime messageTime) {
            this.message = message;
            this.username = username;
            this.messageTime = messageTime;
        }

        public static ChatRes to(Chat chat) {
            return ChatRes.builder()
                .message(chat.getMessage())
                .username(chat.getSender().getUsername())
                .messageTime(chat.getCreatedAt())
                .build();
        }
    }
}