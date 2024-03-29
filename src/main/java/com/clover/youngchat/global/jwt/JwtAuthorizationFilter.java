package com.clover.youngchat.global.jwt;

import static com.clover.youngchat.global.exception.ResultCode.ACCESS_DENY;
import static com.clover.youngchat.global.jwt.JwtUtil.ACCESS_TOKEN_HEADER;
import static com.clover.youngchat.global.jwt.JwtUtil.BEARER_PREFIX;
import static com.clover.youngchat.global.jwt.JwtUtil.REFRESH_TOKEN_HEADER;

import com.clover.youngchat.domain.auth.service.BlacklistService;
import com.clover.youngchat.global.exception.GlobalException;
import com.clover.youngchat.global.redis.RedisUtil;
import com.clover.youngchat.global.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final BlacklistService blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtil.getTokenFromHeader(request, ACCESS_TOKEN_HEADER);
        String refreshToken = jwtUtil.getTokenFromHeader(request, REFRESH_TOKEN_HEADER);

        if (!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (blacklistService.isTokenBlackListed(accessToken)) {
            throw new GlobalException(ACCESS_DENY);
        }

        if (!jwtUtil.validateToken(accessToken)) {
            accessToken = createNewAccessToken(refreshToken);
            response.addHeader("AccessToken", BEARER_PREFIX + accessToken);
        }
        try {
            setAuthentication(jwtUtil.getUserInfoFromToken(accessToken));
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private String createNewAccessToken(String refreshToken) {
        String newAccessToken = "";
        if (!redisUtil.hasKey(refreshToken)) {
            throw new GlobalException(ACCESS_DENY);
        }
        if (StringUtils.hasText(refreshToken) && jwtUtil.validateToken(refreshToken)) {
            String email = (String) redisUtil.get(refreshToken);
            newAccessToken = jwtUtil.createAccessToken(email)
                .split(" ")[1].trim();
        }

        return newAccessToken;
    }

    private Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
    }
}
