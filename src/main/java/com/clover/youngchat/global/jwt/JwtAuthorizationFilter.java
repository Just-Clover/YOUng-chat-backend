package com.clover.youngchat.global.jwt;

import static com.clover.youngchat.global.jwt.JwtUtil.ACCESS_TOKEN_HEADER;
import static com.clover.youngchat.global.jwt.JwtUtil.BEARER_PREFIX;
import static com.clover.youngchat.global.jwt.JwtUtil.REFRESH_TOKEN_HEADER;

import com.clover.youngchat.global.redis.RedisUtil;
import com.clover.youngchat.global.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtil.getTokenFromHeader(request, ACCESS_TOKEN_HEADER);

        if (StringUtils.hasText(accessToken)) {
            accessToken = null;
        }
        if (StringUtils.hasText(accessToken) && !jwtUtil.validateToken(accessToken)) {
            String refreshToken = jwtUtil.getTokenFromHeader(request, REFRESH_TOKEN_HEADER);
            if (StringUtils.hasText(refreshToken) && jwtUtil.validateToken(refreshToken)
                && redisUtil.hasKey(refreshToken)) {
                String email = (String) redisUtil.get(refreshToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                accessToken = jwtUtil.createAccessToken(userDetails.getUsername())
                    .split(" ")[1].trim();

                response.addHeader("AccessToken", BEARER_PREFIX + accessToken);
            }
        }

        if (StringUtils.hasText(accessToken)) {
            Claims info = jwtUtil.getUserInfoFromToken(accessToken);
            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
    }
}
