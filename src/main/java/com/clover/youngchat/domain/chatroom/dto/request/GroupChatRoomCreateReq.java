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

    @Builder
    private GroupChatRoomCreateReq(List<Long> friendIds) {
        this.friendIds = friendIds;
    }
}
