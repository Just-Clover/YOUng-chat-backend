package com.clover.youngchat.global.security;

import com.clover.youngchat.domain.user.dto.response.UserLogoutRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j(topic = "logout Success Handle")
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(final HttpServletRequest request,
        final HttpServletResponse response, final Authentication authentication) {
        SecurityContextHolder.clearContext();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try {
            response.getWriter()
                .println(new ObjectMapper().writeValueAsString(new UserLogoutRes()));
        } catch (IOException e) {
            log.error("Response writing failed: {}", e.getMessage());
        }
    }
}
