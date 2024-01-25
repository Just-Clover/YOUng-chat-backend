package com.clover.youngchat.domain.chat.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatCreateReq {

    private String message;
    private Long userId;

    @Builder
    private ChatCreateReq(String message, Long userId) {
        this.message = message;
        this.userId = userId;
    }
}
