package com.clover.youngchat.domain.user.controller;

import com.clover.youngchat.domain.user.dto.request.UserEmailAuthCheckReq;
import com.clover.youngchat.domain.user.dto.request.UserEmailAuthReq;
import com.clover.youngchat.domain.user.dto.request.UserProfileEditReq;
import com.clover.youngchat.domain.user.dto.request.UserSignupReq;
import com.clover.youngchat.domain.user.dto.request.UserUpdatePasswordReq;
import com.clover.youngchat.domain.user.dto.response.UserEmailAuthCheckRes;
import com.clover.youngchat.domain.user.dto.response.UserEmailAuthRes;
import com.clover.youngchat.domain.user.dto.response.UserProfileEditRes;
import com.clover.youngchat.domain.user.dto.response.UserProfileGetRes;
import com.clover.youngchat.domain.user.dto.response.UserProfileSearchRes;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public RestResponse<UserProfileGetRes> getProfile(@RequestParam(required = false) Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return RestResponse.success(userService.getProfile(userId, userDetails.getUser()));
    }

    @GetMapping(value = "/search", headers = "Keyword")
    public RestResponse<UserProfileSearchRes> searchProfile(
        @RequestHeader("Keyword") String email) {
        return RestResponse.success(userService.searchProfile(email));
    }

    @PostMapping("/signup")
    public RestResponse<UserSignupRes> signup(@Valid @RequestBody UserSignupReq userSignupReq) {
        return RestResponse.success(userService.signup(userSignupReq));
    }

    @PostMapping("/signup/email")
    public RestResponse<UserEmailAuthRes> sendAuthEmail(
        @Valid @RequestBody UserEmailAuthReq req) {
        return RestResponse.success(userService.sendAuthEmail(req));
    }

    @PatchMapping("/profile")
    public RestResponse<UserProfileEditRes> editProfile(@RequestParam Long userId,
        @Valid @RequestPart UserProfileEditReq req,
        @RequestPart(name = "image", required = false) MultipartFile multipartFile,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return RestResponse.success(
            userService.editProfile(userId, req, multipartFile, userDetails.getUser().getId()));
    }

    @PatchMapping("/password")
    public RestResponse<UserUpdatePasswordRes> updatePassword(
        @Valid @RequestBody UserUpdatePasswordReq userUpdatePasswordReq,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return RestResponse.success(
            userService.updatePassword(userDetails.getUser().getId(), userUpdatePasswordReq));
    }

    @PatchMapping("/signup/email")
    public RestResponse<UserEmailAuthCheckRes> checkAuthEmail(
        @RequestBody UserEmailAuthCheckReq req) {
        return RestResponse.success(userService.checkAuthEmail(req));
    }
}
