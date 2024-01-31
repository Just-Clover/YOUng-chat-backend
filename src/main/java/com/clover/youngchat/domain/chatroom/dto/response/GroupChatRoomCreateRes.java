package com.clover.youngchat.domain.chatroom.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupChatRoomCreateRes {

    private Long chatRoomId;
    private String title;

    @Builder
    private GroupChatRoomCreateRes(Long chatRoomId, String title) {
        this.chatRoomId = chatRoomId;
        this.title = title;
    }

    public static GroupChatRoomCreateRes to(Long chatRoomId, String title) {
        return GroupChatRoomCreateRes.builder()
            .chatRoomId(chatRoomId)
            .title(title)
            .build();
    }
}
