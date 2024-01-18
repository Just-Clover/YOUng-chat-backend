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
    private List<ChatRes> chatResList = new ArrayList<>();

    @Builder
    private ChatRoomDetailGetRes(String title, List<ChatRes> chatResList) {
        this.title = title;
        this.chatResList = chatResList;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ChatRes {

        Long userId;
        String message;
        LocalDateTime messageTime;

        @Builder
        private ChatRes(Long userId, String message, LocalDateTime messageTime) {
            this.userId = userId;
            this.message = message;
            this.messageTime = messageTime;
        }

        public static ChatRes to(Chat chat) {
            return ChatRes.builder()
                .userId(chat.getSender().getId())
                .message(chat.getMessage())
                .messageTime(chat.getCreatedAt())
                .build();
        }
    }
}