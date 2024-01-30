package com.clover.youngchat.domain.chatroom.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PrivateChatRoomCreateRes {

    private Long chatRoomId;
    private String title;

    @Builder
    private PrivateChatRoomCreateRes(Long chatRoomId, String title) {
        this.chatRoomId = chatRoomId;
        this.title = title;
    }

    public static PrivateChatRoomCreateRes to(Long chatRoomId, String title) {
        return PrivateChatRoomCreateRes.builder()
            .chatRoomId(chatRoomId)
            .title(title)
            .build();
    }
}
