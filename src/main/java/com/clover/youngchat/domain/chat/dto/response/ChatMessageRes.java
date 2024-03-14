package com.clover.youngchat.domain.chat.dto.response;

import com.clover.youngchat.domain.chat.entity.Chat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageRes {

    private String chatId;
    private Long userId;
    private String message;
    private Boolean isDeleted;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime messageTime;

    @Builder
    private ChatMessageRes(String chatId, Long userId, String message, Boolean isDeleted,
        LocalDateTime messageTime) {
        this.chatId = chatId;
        this.userId = userId;
        this.message = message;
        this.isDeleted = isDeleted;
        this.messageTime = messageTime;
    }


    public static ChatMessageRes to(Chat chat) {
        return ChatMessageRes.builder()
            .chatId(chat.getId())
            .userId(chat.getSenderId())
            .message(chat.getMessage())
            .isDeleted(chat.isDeleted())
            .messageTime(chat.getCreatedAt())
            .build();
    }

}
