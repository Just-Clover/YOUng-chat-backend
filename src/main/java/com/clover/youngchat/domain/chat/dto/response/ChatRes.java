package com.clover.youngchat.domain.chat.dto.response;

import com.clover.youngchat.domain.chat.entity.Chat;
import com.clover.youngchat.domain.user.entity.User;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRes {

    private String chatId;
    private Long userId;
    private String username;
    private String profileImage;
    private String message;
    private Boolean isDeleted;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime messageTime;

    @Builder
    public ChatRes(String chatId, Long userId, String username, String profileImage,
        String message, Boolean isDeleted, LocalDateTime messageTime) {
        this.chatId = chatId;
        this.userId = userId;
        this.username = username;
        this.profileImage = profileImage;
        this.message = message;
        this.isDeleted = isDeleted;
        this.messageTime = messageTime;
    }


    public static ChatRes to(Chat chat, User user) {
        return ChatRes.builder()
            .chatId(chat.getId())
            .userId(chat.getSenderId())
            .username(user.getUsername())
            .profileImage(user.getProfileImage())
            .message(chat.getMessage())
            .isDeleted(chat.isDeleted())
            .messageTime(chat.getCreatedAt())
            .build();
    }
}