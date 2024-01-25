package com.clover.youngchat.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEmailCheckRes {

    private boolean duplicated;

    @Builder
    private UserEmailCheckRes(boolean duplicated) {
        this.duplicated = duplicated;
    }

    public static UserEmailCheckRes to(boolean duplicated) {
        return UserEmailCheckRes.builder()
            .duplicated(duplicated)
            .build();
    }
}
