package com.clover.youngchat.domain.auth.entity;

import static com.clover.youngchat.global.jwt.JwtUtil.ACCESS_TOKEN_TIME;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "blacklist", timeToLive = ACCESS_TOKEN_TIME)
@EnableRedisRepositories
public class Blacklist {

    @Id
    private String accessToken;

    @Builder
    private Blacklist(String accessToken) {
        this.accessToken = accessToken;
    }

}
