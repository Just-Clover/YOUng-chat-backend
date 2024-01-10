package com.clover.youngchat.domain.chatroom.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomCreateReq {

    private Long friendId;
    private String title;

    @Builder
    private ChatRoomCreateReq(Long friendId, String title) {
        this.friendId = friendId;
        this.title = title;
    }
}
