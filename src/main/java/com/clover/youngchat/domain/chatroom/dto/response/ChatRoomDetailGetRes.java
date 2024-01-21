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

        private Long chatId;
        private Long userId;
        private String username;
        private String profileImage;
        private String message;
        private boolean isDeleted;
        private LocalDateTime messageTime;

        @Builder
        public ChatRes(Long chatId, Long userId, String username, String profileImage,
            String message, boolean isDeleted, LocalDateTime messageTime) {
            this.chatId = chatId;
            this.userId = userId;
            this.username = username;
            this.profileImage = profileImage;
            this.message = message;
            this.isDeleted = isDeleted;
            this.messageTime = messageTime;
        }

        public static ChatRes to(Chat chat) {
            return ChatRes.builder()
                .chatId(chat.getId())
                .userId(chat.getSender().getId())
                .username(chat.getSender().getUsername())
                .profileImage(chat.getSender().getProfileImage())
                .message(chat.getMessage())
                .isDeleted(chat.isDeleted())
                .messageTime(chat.getCreatedAt())
                .build();
        }
    }
}