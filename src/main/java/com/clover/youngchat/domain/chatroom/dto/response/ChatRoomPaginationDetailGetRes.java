package com.clover.youngchat.domain.chatroom.dto.response;

import com.clover.youngchat.domain.chat.dto.response.ChatRes;
import com.clover.youngchat.global.response.RestSlice;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomPaginationDetailGetRes {

    private String title;
    private RestSlice<ChatRes> chatResList;

    @Builder
    private ChatRoomPaginationDetailGetRes(String title, RestSlice<ChatRes> chatResList) {
        this.title = title;
        this.chatResList = chatResList;
    }
}