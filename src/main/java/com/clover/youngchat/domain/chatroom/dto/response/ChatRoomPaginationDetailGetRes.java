package com.clover.youngchat.domain.chatroom.dto.response;

import com.clover.youngchat.domain.chat.dto.response.ChatRes;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomPaginationDetailGetRes {

    private String title;
    private Slice<ChatRes> chatResList;

    @Builder
    private ChatRoomPaginationDetailGetRes(String title, Slice<ChatRes> chatResList) {
        this.title = title;
        this.chatResList = chatResList;
    }
}