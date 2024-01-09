package com.clover.youngchat.domain.user.controller;

import com.clover.youngchat.domain.user.dto.request.UserSignupReq;
import com.clover.youngchat.domain.user.dto.request.UserUpdatePasswordReq;
import com.clover.youngchat.domain.user.dto.response.UserProfileGetRes;
import com.clover.youngchat.domain.user.dto.response.UserSignupRes;
import com.clover.youngchat.domain.user.dto.response.UserUpdatePasswordRes;
import com.clover.youngchat.domain.user.service.UserService;
import com.clover.youngchat.global.response.RestResponse;
import com.clover.youngchat.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public RestResponse<UserSignupRes> signup(@Valid @RequestBody UserSignupReq userSignupReq) {
        return RestResponse.success(userService.signup(userSignupReq));
    }

    @GetMapping("/profile/{userId}")
    public RestResponse<UserProfileGetRes> getProfile(@PathVariable Long userId) {
        return RestResponse.success(userService.getProfile(userId));
    }

    @PatchMapping("/password")
    public RestResponse<UserUpdatePasswordRes> updatePassword(
        @Valid @RequestBody UserUpdatePasswordReq userUpdatePasswordReq,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return RestResponse.success(
            userService.updatePassword(userDetails.getUser().getId(), userUpdatePasswordReq));
    }
}
