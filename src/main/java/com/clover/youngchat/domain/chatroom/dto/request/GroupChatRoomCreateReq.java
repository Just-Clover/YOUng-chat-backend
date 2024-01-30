package com.clover.youngchat.domain.chatroom.dto.request;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupChatRoomCreateReq {

    private List<Long> friendIds;
    private String title;

    @Builder
    private GroupChatRoomCreateReq(List<Long> friendIds, String title) {
        this.friendIds = friendIds;
        this.title = title;
    }
}
