package com.clover.youngchat.domain.chatroom.dto.response;

import com.clover.youngchat.domain.chat.dto.response.ChatRes;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomDetailGetRes {

    private String title;
    private List<ChatRes> chatResList = new ArrayList<>();

    @Builder
    private ChatRoomDetailGetRes(String title, List<ChatRes> chatResList) {
        this.title = title;
        this.chatResList = chatResList;
    }
}