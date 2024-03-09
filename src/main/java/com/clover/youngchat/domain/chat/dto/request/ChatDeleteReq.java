package com.clover.youngchat.domain.chat.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatDeleteReq {

    private Long userId;
    private String chatId;

    @Builder
    private ChatDeleteReq(Long userId, String chatId) {
        this.userId = userId;
        this.chatId = chatId;
    }
}
