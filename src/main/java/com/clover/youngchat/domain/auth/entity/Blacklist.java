package com.clover.youngchat.domain.auth.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "blacklist")
public class Blacklist {

    @Id
    private String accessToken;

    @TimeToLive
    private long expiration;

    @Builder
    private Blacklist(String accessToken, Long expiration) {
        this.accessToken = accessToken;
        this.expiration = expiration;
    }

}
