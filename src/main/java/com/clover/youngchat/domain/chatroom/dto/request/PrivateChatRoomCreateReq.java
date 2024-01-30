package com.clover.youngchat.domain.chatroom.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PrivateChatRoomCreateReq {

    private Long friendId;
    private String title;

    @Builder
    private PrivateChatRoomCreateReq(Long friendId, String title) {
        this.friendId = friendId;
        this.title = title;
    }
}
