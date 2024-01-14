package com.clover.youngchat.domain.chatroom.dto.request;

import static com.clover.youngchat.domain.chatroom.constant.ChatRoomConstant.NOT_NULL_CHATROOM_TITLE;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomCreateReq {

    private Long friendId;

    @NotNull(message = NOT_NULL_CHATROOM_TITLE)
    private String title;

    @Builder
    private ChatRoomCreateReq(Long friendId, String title) {
        this.friendId = friendId;
        this.title = title;
    }
}
