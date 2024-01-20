package com.clover.youngchat.domain.chatroom.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomCreateRes {

    private Long chatRoomId;
    private String title;

    @Builder
    private ChatRoomCreateRes(Long chatRoomId, String title) {
        this.chatRoomId = chatRoomId;
        this.title = title;
    }

    public static ChatRoomCreateRes to(Long chatRoomId, String title) {
        return ChatRoomCreateRes.builder()
            .chatRoomId(chatRoomId)
            .title(title)
            .build();
    }
}
