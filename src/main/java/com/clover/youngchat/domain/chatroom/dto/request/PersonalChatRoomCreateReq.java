package com.clover.youngchat.domain.chatroom.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonalChatRoomCreateReq {

    private Long friendId;

    @Builder
    private PersonalChatRoomCreateReq(Long friendId) {
        this.friendId = friendId;
    }
}
