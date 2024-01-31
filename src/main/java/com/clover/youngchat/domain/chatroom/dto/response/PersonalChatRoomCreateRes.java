package com.clover.youngchat.domain.chatroom.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonalChatRoomCreateRes {

    private Long chatRoomId;
    private String title;

    @Builder
    private PersonalChatRoomCreateRes(Long chatRoomId, String title) {
        this.chatRoomId = chatRoomId;
        this.title = title;
    }

    public static PersonalChatRoomCreateRes to(Long chatRoomId, String title) {
        return PersonalChatRoomCreateRes.builder()
            .chatRoomId(chatRoomId)
            .title(title)
            .build();
    }
}
