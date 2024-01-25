package com.clover.youngchat.domain.friend.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendGetListRes {

    private Long userId;
    private String username;
    private String profileImage;

    @Builder
    private FriendGetListRes(Long userId, String username, String profileImage) {
        this.userId = userId;
        this.username = username;
        this.profileImage = profileImage;
    }

    public static FriendGetListRes to(Long userId, String username, String profileImage) {
        return FriendGetListRes.builder()
            .userId(userId)
            .username(username)
            .profileImage(profileImage)
            .build();
    }
}
