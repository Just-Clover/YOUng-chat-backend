package com.clover.youngchat.domain.friend.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendGetListRes {

    private String username;
    private String profileImage;

    @Builder
    private FriendGetListRes(String username, String profileImage) {
        this.username = username;
        this.profileImage = profileImage;
    }

    public static FriendGetListRes to(String username, String profileImage) {
        return FriendGetListRes.builder()
            .username(username)
            .profileImage(profileImage)
            .build();
    }
}
