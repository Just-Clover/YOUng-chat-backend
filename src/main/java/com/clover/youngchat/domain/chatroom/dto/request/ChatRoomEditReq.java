package com.clover.youngchat.domain.chatroom.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomEditReq {

    private String title;

    @Builder
    private ChatRoomEditReq(String title) {
        this.title = title;
    }
}
