package com.clover.youngchat.domain.chatroom.dto.response;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomIdAndTitleGetRes {

    private Long chatRoomId;
    private String title;

    public ChatRoomIdAndTitleGetRes(Long chatRoomId, String title) {
        this.chatRoomId = chatRoomId;
        this.title = title;
    }
}
