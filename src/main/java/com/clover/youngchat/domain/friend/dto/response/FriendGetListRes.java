package com.clover.youngchat.domain.friend.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendGetListRes {

    List<String> usernameList;

    @Builder
    private FriendGetListRes(List<String> usernameList) {
        this.usernameList = usernameList;
    }

    public static FriendGetListRes to(List<String> usernameList) {
        return FriendGetListRes.builder()
            .usernameList(usernameList)
            .build();
    }
}
