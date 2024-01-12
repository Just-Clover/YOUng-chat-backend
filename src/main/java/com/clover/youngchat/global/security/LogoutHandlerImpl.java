package com.clover.youngchat.global.security;

import static com.clover.youngchat.global.jwt.JwtUtil.ACCESS_TOKEN_HEADER;
import static com.clover.youngchat.global.jwt.JwtUtil.REFRESH_TOKEN_HEADER;

import com.clover.youngchat.domain.auth.service.BlacklistService;
import com.clover.youngchat.global.jwt.JwtUtil;
import com.clover.youngchat.global.redis.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class LogoutHandlerImpl implements LogoutHandler {

    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final BlacklistService blacklistService;

    @Override
    public void logout(final HttpServletRequest request, final HttpServletResponse response,
        final Authentication authentication) {
        String refreshToken = jwtUtil.getTokenFromHeader(request, REFRESH_TOKEN_HEADER);
        String accessToken = jwtUtil.getTokenFromHeader(request, ACCESS_TOKEN_HEADER);

        if (StringUtils.hasText(refreshToken)) {
            redisUtil.delete(refreshToken);
        }

        long expiration = jwtUtil.getExpirationSecondsFromToken(accessToken);
        blacklistService.addTokenToBlacklist(accessToken, expiration);
    }
}
